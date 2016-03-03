package com.redhat.ceylon.model.loader.model;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.loader.ContentAwareArtifactResult;
import com.redhat.ceylon.model.loader.JdkProvider;
import com.redhat.ceylon.model.loader.JvmBackendUtil;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Package;

/**
 * Represents a lazy Module declaration.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public abstract class LazyModule extends Module {

    public static interface PackagePathsProvider {
        Set<String> getPackagePaths();
    }
    
    private boolean isJava = false;
    protected Set<String> jarPackages = new HashSet<String>();
    private PackagePathsProvider packagePathsProvider;
    
    /**
     * Set of exported Java9 Module packages, or null if not a Java9 module. Only
     * set if this is not a Ceylon module.
     */
    private List<String> exportedJavaPackages;

    public LazyModule() {
    }

    @Override
    public Package getDirectPackage(String name) {
        return findPackageInModule(this, name);
    }
    
    @Override
    public Package getPackage(String name) {
        // try here first
        Package pkg = null;
        
        // unless we're the default module, in which case we have to check this at the end,
        // since every package can be part of the default module
        boolean defaultModule = isDefault();
        if(!defaultModule){
            pkg = findPackageInModule(this, name);
            if(pkg != null)
                return pkg;
        }
        // then try in dependencies
        Set<Module> visited = new HashSet<Module>();
        for(ModuleImport dependency : getImports()){
            // we don't have to worry about the default module here since we can't depend on it
            pkg = findPackageInImport(name, dependency, visited);
            if(pkg != null)
                return pkg;
        }
        AbstractModelLoader modelLoader = getModelLoader();
        JdkProvider jdkProvider = modelLoader.getJdkProvider();
        // The JDK uses arrays, which we pretend are in java.lang, and ByteArray needs ceylon.language.Byte,
        // so we pretend the JDK imports the language module
        if(jdkProvider.isJDKModule(getNameAsString())){
            Module languageModule = getModelLoader().getLanguageModule();
            if(languageModule instanceof LazyModule){
                pkg = findPackageInModule((LazyModule) languageModule, name);
                if(pkg != null)
                    return pkg;
            }
        }
        // never try to load java packages from the default module because it would
        // work and appear to come from there
        if(jdkProvider.isJDKPackage(name)){
            return null;
        }
        // do the lookup of the default module last
        if(defaultModule)
            pkg = modelLoader.findExistingPackage(this, name);
        return pkg;
    }

    private Package findPackageInImport(String name, ModuleImport dependency, Set<Module> visited) {
        Module module = dependency.getModule();
        // only visit modules once
        if(!visited.add(module))
            return null;
        if (module instanceof LazyModule) {
            // this is the equivalent of getDirectPackage, it does not recurse
            Package pkg =  findPackageInModule((LazyModule) dependency.getModule(), name);
            if(pkg != null)
                return pkg;
            // not found, try in its exported dependencies
            for(ModuleImport dep : module.getImports()){
                if(!dep.isExport())
                    continue;
                pkg = findPackageInImport(name, dep, visited);
                if(pkg != null)
                    return pkg;
            }
            // not found
            return null;
        }
        else
            return module.getPackage(name);
    }

    private Package findPackageInModule(LazyModule module, String name) {
        if(module.containsPackage(name)){
            // first try the already loaded packages from that module
            AbstractModelLoader modelLoader = getModelLoader();
            synchronized(modelLoader.getLock()){
                for(Package pkg : module.getPackages()){
                    if(pkg.getNameAsString().equals(name))
                        return pkg;
                }
                // create/load a new one
                return getModelLoader().findExistingPackage(module, name);
            }
        }
        return null;
    }

    public Package findPackageNoLazyLoading(String name) {
        // make sure we don't call any overloaded getPackages() that might trigger lazy loading
        for(Package pkg : super.getPackages()){
            if(pkg.getNameAsString().equals(name))
                return pkg;
        }
        return null;
    }

    protected abstract AbstractModelLoader getModelLoader();

    public boolean isJava() {
        return isJava;
    }

    public void setJava(boolean isJava) {
        this.isJava = isJava;
    }

    public void loadPackageList(ArtifactResult artifact) {
        if (artifact instanceof ContentAwareArtifactResult) {
            for (String entry : ((ContentAwareArtifactResult) artifact).getPackages()) {
                String pkg = entry.replace('.', '/');
                // make sure we unquote any package part
                pkg = pkg.replace("$", "");
                String pathQuery;
                if(entry.isEmpty())
                    pathQuery = pkg;
                else
                    pathQuery = pkg+"/";
                if(artifact.filter() == null || artifact.filter().accept(pathQuery))
                    jarPackages.add(entry);
            }
        } else {
            File file = artifact.artifact();
            if (file != null) {
                try {
                    jarPackages.addAll(JvmBackendUtil.listPackages(file, artifact.filter()));
                } catch (IOException e) {
                    throw new RuntimeException("Error accessing artifact: " + artifact.artifact(), e);
                }
            }
        }
    }

    public boolean containsPackage(String pkgName){
        String moduleName = getNameAsString();
        if(!isJava){
            List<Package> superPackages = super.getPackages();
            for(int i=0,l=superPackages.size();i<l;i++){
                if(superPackages.get(i).getNameAsString().equals(pkgName))
                    return true;
            }
            // The language module is in the classpath and does not have its jarPackages loaded
            if(moduleName.equals(Module.LANGUAGE_MODULE_NAME)){
                return JvmBackendUtil.isSubPackage(moduleName, pkgName)
                        || pkgName.startsWith("com.redhat.ceylon.compiler.java.runtime")
                        || pkgName.startsWith("com.redhat.ceylon.compiler.java.language")
                        || pkgName.startsWith("com.redhat.ceylon.compiler.java.metadata");
            }
            return getJarPackages().contains(pkgName);
        }else{
            // special rules for the JDK which we don't load from the repo
            if(isJdkPackage(moduleName, pkgName))
                return true;
            JdkProvider jdkProvider = getModelLoader().getJdkProvider();
            if(jdkProvider.getJdkContainerModule() == this
                    && jdkProvider.isJDKPackage(pkgName)){
                return false;
            }
            // otherwise we have the list of packages contained in that module jar
            return getJarPackages().contains(pkgName);
        }
    }
    
    @Override
    protected boolean isJdkModule(String moduleName) {
        return getModelLoader().getJdkProvider().isJDKModule(moduleName);
    }
    
    @Override
    protected boolean isJdkPackage(String moduleName, String packageName) {
        return getModelLoader().getJdkProvider().isJDKPackage(moduleName, packageName);
    }

    public void addPackage(Package pkg){
        // make sure we don't call any overloaded getPackages() that might trigger lazy loading
        super.getPackages().add(pkg);
    }

    public boolean isExportedJavaPackage(String name) {
        return exportedJavaPackages != null ? exportedJavaPackages.contains(name) : true;
    }

    public void setExportedJavaPackages(List<String> exportedPackages) {
        this.exportedJavaPackages = exportedPackages;
    }

    private void loadPackageListFromPackagePaths(Set<String> packagePaths) {
        for(String pkg : packagePaths){
            // make sure we unquote any package part
            pkg = pkg.replace("$", "");
            pkg = pkg.replace('/', '.');
            jarPackages.add(pkg);
        }
    }
    
    protected Set<String> getJarPackages(){
        if(packagePathsProvider != null){
            loadPackageListFromPackagePaths(packagePathsProvider.getPackagePaths());
            packagePathsProvider = null;
        }
        return jarPackages;
    }

    public void setPackagePathsProvider(PackagePathsProvider packagePathsProvider) {
        this.packagePathsProvider = packagePathsProvider;
    }
}

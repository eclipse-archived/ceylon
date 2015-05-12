package com.redhat.ceylon.model.loader.model;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.JDKUtils;
import com.redhat.ceylon.model.cmr.PathFilter;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.loader.ContentAwareArtifactResult;
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

    private boolean isJava = false;
    protected Set<String> jarPackages = new HashSet<String>();

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
        // The JDK uses arrays, which we pretend are in java.lang, and ByteArray needs ceylon.language.Byte,
        // so we pretend the JDK imports the language module
        if(JDKUtils.isJDKModule(getNameAsString()) || JDKUtils.isOracleJDKModule(getNameAsString())){
            Module languageModule = getModelLoader().getLanguageModule();
            if(languageModule instanceof LazyModule){
                pkg = findPackageInModule((LazyModule) languageModule, name);
                if(pkg != null)
                    return pkg;
            }
        }
        // never try to load java packages from the default module because it would
        // work and appear to come from there
        if(JDKUtils.isJDKAnyPackage(name) || JDKUtils.isOracleJDKAnyPackage(name)){
            return null;
        }
        // do the lookup of the default module last
        if(defaultModule)
            pkg = getModelLoader().findExistingPackage(this, name);
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
            for (String entry : ((ContentAwareArtifactResult) artifact).getEntries()) {
                addPackageForPath(entry, artifact.filter());
            }
        } else {
            File file = artifact.artifact();
            if (file != null) {
                ZipFile zipFile;
                try {
                    zipFile = new ZipFile(artifact.artifact());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try{
                    Enumeration<? extends ZipEntry> entries = zipFile.entries();
                    while(entries.hasMoreElements())
                        loadPackageList(entries.nextElement(), artifact.filter());
                }finally{
                    try {
                        zipFile.close();
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }
        }
    }
    
    private void loadPackageList(ZipEntry entry, PathFilter pathFilter) {
        if(!entry.isDirectory()){
            String path = entry.getName();
            addPackageForPath(path, pathFilter);
        }
    }

    private void addPackageForPath(String path, PathFilter pathFilter) {
        if(path.toLowerCase().endsWith(".class")){
            int sep = path.lastIndexOf('/');
            if(sep != -1)
                path = path.substring(0, sep);
            String pkg = path;
            // make sure we unquote any package part
            pkg = pkg.replace("$", "");
            String pathQuery;
            if(path.isEmpty())
                pathQuery = pkg;
            else
                pathQuery = pkg+"/";
            if(pathFilter == null || pathFilter.accept(pathQuery)){
                pkg = pkg.replace('/', '.');
                jarPackages.add(pkg);
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
            return jarPackages.contains(pkgName);
        }else{
            // special rules for the JDK which we don't load from the repo
            if(JDKUtils.isJDKPackage(moduleName, pkgName)
                    || JDKUtils.isOracleJDKPackage(moduleName, pkgName))
                return true;
            // otherwise we have the list of packages contained in that module jar
            return jarPackages.contains(pkgName);
        }
    }
    
    @Override
    protected boolean isJdkModule(String moduleName) {
        return JDKUtils.isJDKModule(moduleName) || JDKUtils.isOracleJDKModule(moduleName);
    }
    
    @Override
    protected boolean isJdkPackage(String moduleName, String packageName) {
        return JDKUtils.isJDKPackage(moduleName, packageName)
                || JDKUtils.isOracleJDKPackage(moduleName, packageName);
    }

    public void addPackage(Package pkg){
        // make sure we don't call any overloaded getPackages() that might trigger lazy loading
        super.getPackages().add(pkg);
    }
}

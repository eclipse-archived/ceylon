/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.loader.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.JDKUtils;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.loader.ContentAwareArtifactResult;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Package;

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
        // never try to load java packages from the default module because it would
        // work and appear to come from there
        if(JDKUtils.isJDKAnyPackage(name) || JDKUtils.isOracleJDKAnyPackage(name))
            return null;
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
                addPackageForPath(entry);
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
                        loadPackageList(entries.nextElement());
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
    
    private void loadPackageList(ZipEntry entry) {
        if(!entry.isDirectory()){
            String path = entry.getName();
            addPackageForPath(path);
        }
    }

    private void addPackageForPath(String path) {
        if(path.toLowerCase().endsWith(".class")){
            int sep = path.lastIndexOf('/');
            if(sep != -1)
                path = path.substring(0, sep);
            String pkg = path.replace('/', '.');
            // make sure we unquote any package part
            pkg = pkg.replace("$", "");
            jarPackages.add(pkg);
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
                return Util.isSubPackage(moduleName, pkgName);
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

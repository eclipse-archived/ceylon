package com.redhat.ceylon.compiler.java.runtime.tools.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.RepositoryException;
import com.redhat.ceylon.model.typechecker.model.Module;

public class FlatpathModuleLoader extends BaseModuleLoaderImpl {
    final Map<String, String> extraModules;

    public FlatpathModuleLoader() {
        this(null, null, null);
    }

    public FlatpathModuleLoader(ClassLoader delegateClassLoader) {
        this(null, delegateClassLoader, null);
    }

    public FlatpathModuleLoader(RepositoryManager repositoryManager,
            ClassLoader delegateClassLoader, Map<String, String> extraModules) {
        super(repositoryManager, delegateClassLoader);
        this.extraModules = extraModules;
    }

    class FlatpathModuleLoaderContext extends ModuleLoaderContext {

        FlatpathModuleLoaderContext(String module, String version) {
            super(module, version);
        }

        @Override
        void initialise() {
            preloadModules();
            moduleClassLoader = setupClassLoader();
            initialiseMetamodel();
        }

        private void preloadModules() {
            try {
                // those come from the delegate class loader
                loadModule(Module.LANGUAGE_MODULE_NAME, Versions.CEYLON_VERSION_NUMBER, false, true);
                loadModule("com.redhat.ceylon.compiler.java", Versions.CEYLON_VERSION_NUMBER, false, true);
                loadModule("com.redhat.ceylon.common", Versions.CEYLON_VERSION_NUMBER, false, true);
                loadModule("com.redhat.ceylon.model", Versions.CEYLON_VERSION_NUMBER, false, true);
                loadModule("com.redhat.ceylon.module-resolver", Versions.CEYLON_VERSION_NUMBER, false, true);
                loadModule("com.redhat.ceylon.typechecker", Versions.CEYLON_VERSION_NUMBER, false, true);
                // these ones not necessarily
                loadModule(module, modver, false, false);
                if(extraModules != null){
                    for(Entry<String,String> entry : extraModules.entrySet()){
                        loadModule(entry.getKey(), entry.getValue(), false, false);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void initialiseMetamodel() {
            Set<String> registered = new HashSet<String>();
            registerInMetamodel("ceylon.language", registered);
            registerInMetamodel("com.redhat.ceylon.typechecker", registered);
            registerInMetamodel("com.redhat.ceylon.common", registered);
            registerInMetamodel("com.redhat.ceylon.model", registered);
            registerInMetamodel("com.redhat.ceylon.module-resolver", registered);
            registerInMetamodel("com.redhat.ceylon.compiler.java", registered);
            registerInMetamodel(module, registered);
            if(extraModules != null){
                for(String extraModule : extraModules.keySet()){
                    registerInMetamodel(extraModule, registered);
                }
            }
        }
        
        private ClassLoader setupClassLoader() {
            // make a Class loader for this module if required
            if(loadedModulesInCurrentClassLoader.contains(module))
                return delegateClassLoader;
            else
                return makeModuleClassLoader();
        }
        
        private ClassLoader makeModuleClassLoader() {
            // we need to make a class loader for all the modules it requires which are not provided by the current class loader
            Set<String> modulesNotInCurrentClassLoader = new HashSet<String>();
            for(Entry<String, ArtifactResult> entry : loadedModules.entrySet()){
                if(entry.getValue() != null)
                    modulesNotInCurrentClassLoader.add(entry.getKey());
            }
            modulesNotInCurrentClassLoader.removeAll(loadedModulesInCurrentClassLoader);
            URL[] urls = new URL[modulesNotInCurrentClassLoader.size()];
            int i=0;
            for(String module : modulesNotInCurrentClassLoader){
                ArtifactResult artifact = loadedModules.get(module);
                try {
                    @SuppressWarnings("deprecation")
                    URL url = artifact.artifact().toURL();
                    urls[i++] = url;
                } catch (MalformedURLException | RepositoryException e) {
                    throw new RuntimeException("Failed to get a URL for module file for "+module, e);
                }
            }
            return new URLClassLoader(urls , delegateClassLoader);
        }
    }

    @Override
    ModuleLoaderContext createModuleLoaderContext(String name, String version) {
        return new FlatpathModuleLoaderContext(name, version);
    }
}

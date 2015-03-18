package com.redhat.ceylon.compiler.java.runtime.tools.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.compiler.typechecker.model.Module;

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

        void preloadModules() {
            try {
                // those come from the delegate class loader
                loadModule(Module.LANGUAGE_MODULE_NAME, Versions.CEYLON_VERSION_NUMBER, false, true);
                loadModule("com.redhat.ceylon.compiler.java", Versions.CEYLON_VERSION_NUMBER, false, true);
                loadModule("com.redhat.ceylon.common", Versions.CEYLON_VERSION_NUMBER, false, true);
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

        void initialiseMetamodel() {
            Set<String> registered = new HashSet<String>();
            registerInMetamodel("ceylon.language", registered);
            registerInMetamodel("com.redhat.ceylon.typechecker", registered);
            registerInMetamodel("com.redhat.ceylon.common", registered);
            registerInMetamodel("com.redhat.ceylon.module-resolver", registered);
            registerInMetamodel("com.redhat.ceylon.compiler.java", registered);
            registerInMetamodel(module, registered);
            if(extraModules != null){
                for(String extraModule : extraModules.keySet()){
                    registerInMetamodel(extraModule, registered);
                }
            }
        }
    }

    @Override
    ModuleLoaderContext createModuleLoaderContext(String name, String version) {
        return new FlatpathModuleLoaderContext(name, version);
    }
}

package com.redhat.ceylon.compiler.java.runtime.tools.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.cmr.api.RepositoryManager;

public class JBossModuleLoader extends BaseModuleLoaderImpl {

    public JBossModuleLoader() {
        this(null, null);
    }

    public JBossModuleLoader(ClassLoader delegateClassLoader) {
        this(null, delegateClassLoader);
    }

    public JBossModuleLoader(RepositoryManager repositoryManager, ClassLoader delegateClassLoader) {
        super(repositoryManager, delegateClassLoader);
    }

    class JBossModuleLoaderContext extends ModuleLoaderContext {

        JBossModuleLoaderContext(String module, String version) {
            super(module, version);
        }

        void preloadModules() {
            try {
                loadModule(module, modver, false, false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        void initialiseMetamodel() {
            Set<String> registered = new HashSet<String>();
            registerInMetamodel(module, registered);
        }
    }

    @Override
    ModuleLoaderContext createModuleLoaderContext(String name, String version) {
        return new JBossModuleLoaderContext(name, version);
    }
}

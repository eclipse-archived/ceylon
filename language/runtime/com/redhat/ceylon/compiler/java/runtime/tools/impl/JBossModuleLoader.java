package com.redhat.ceylon.compiler.java.runtime.tools.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;

import com.redhat.ceylon.cmr.api.RepositoryManager;

public class JBossModuleLoader extends BaseModuleLoaderImpl {

    public JBossModuleLoader() {
        this(null);
    }

    public JBossModuleLoader(ClassLoader delegateClassLoader) {
        this(null, delegateClassLoader, false);
    }

    public JBossModuleLoader(RepositoryManager repositoryManager, ClassLoader delegateClassLoader, boolean verbose) {
        super(repositoryManager, delegateClassLoader, verbose);
    }

    class JBossModuleLoaderContext extends ModuleLoaderContext {
        ModuleLoader modLoader;

        JBossModuleLoaderContext(String module, String version) {
            super(module, version);
        }

        @Override
        void initialise() {
            preloadModules();
            initialiseMetamodel();
            moduleClassLoader = setupClassLoader();
        }
        
        private void preloadModules() {
            try {
                loadModule(module, modver, false, false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private ClassLoader setupClassLoader() {
            if (delegateClassLoader != null) {
                modLoader = ModuleLoader.forClassLoader(delegateClassLoader);
            } else {
                modLoader = ModuleLoader.forClass(JBossModuleLoader.class);
            }
            ModuleIdentifier modid = ModuleIdentifier.create(module, modver);
            try {
                Module mod = modLoader.loadModule(modid);
                return mod.getClassLoader();
            } catch (ModuleLoadException e) {
                throw new RuntimeException("Could not load module " + modid, e);
            }
        }

        private void initialiseMetamodel() {
            Set<String> registered = new HashSet<String>();
            registerInMetamodel(module, registered);
        }
    }

    @Override
    ModuleLoaderContext createModuleLoaderContext(String name, String version) {
        return new JBossModuleLoaderContext(name, version);
    }
}

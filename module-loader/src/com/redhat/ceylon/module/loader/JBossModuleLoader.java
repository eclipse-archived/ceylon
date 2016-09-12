package com.redhat.ceylon.module.loader;

import java.io.IOException;

import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;

import com.redhat.ceylon.cmr.api.RepositoryManager;

public class JBossModuleLoader extends BaseModuleLoaderImpl {

    public JBossModuleLoader() {
        this(null, null);
    }

    /*
     * Used by reflection in com.redhat.ceylon.common.tool.ToolLoader
     */
    public JBossModuleLoader(RepositoryManager repoManager, ClassLoader delegateClassLoader) {
        this(repoManager, delegateClassLoader, false);
    }

    public JBossModuleLoader(RepositoryManager repositoryManager, ClassLoader delegateClassLoader, boolean verbose) {
        super(repositoryManager, delegateClassLoader, verbose);
    }

    class JBossModuleLoaderContext extends ModuleLoaderContext {
        ModuleLoader modLoader;

        JBossModuleLoaderContext(String module, String version) throws ModuleNotFoundException {
            super(module, version);
        }

        @Override
        void initialise() throws ModuleNotFoundException {
            preloadModules();
            initialiseMetamodel();
            moduleClassLoader = setupClassLoader();
        }
        
        private void preloadModules() throws ModuleNotFoundException{
            try {
                loadModule(null, module, modver, false, false, null);
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
    }

    @Override
    ModuleLoaderContext createModuleLoaderContext(String name, String version) throws ModuleNotFoundException {
        return new JBossModuleLoaderContext(name, version);
    }
}

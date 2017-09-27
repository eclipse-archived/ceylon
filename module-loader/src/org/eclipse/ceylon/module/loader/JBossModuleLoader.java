package org.eclipse.ceylon.module.loader;

import java.util.Map;

import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.ceylon.loader.ModuleNotFoundException;
import org.eclipse.ceylon.model.cmr.ModuleScope;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;

public class JBossModuleLoader extends BaseRuntimeModuleLoaderImpl {

    public JBossModuleLoader() {
        this(null, null);
    }

    /*
     * Used by reflection in org.eclipse.ceylon.common.tool.ToolLoader
     */
    public JBossModuleLoader(RepositoryManager repoManager, ClassLoader delegateClassLoader) {
        super(repoManager, delegateClassLoader);
    }

    public JBossModuleLoader(RepositoryManager repositoryManager, ClassLoader delegateClassLoader, 
            Map<String,String> extraModules, boolean verbose) {
        super(repositoryManager, delegateClassLoader, extraModules, verbose);
    }

    class JBossModuleLoaderContext extends RuntimeModuleLoaderContext {
        ModuleLoader modLoader;

        JBossModuleLoaderContext(String module, String version, ModuleScope lookupScope) throws ModuleNotFoundException {
            super(module, version, lookupScope);
        }

        @Override
        protected void initialise() throws ModuleNotFoundException {
            preloadModules();
            initialiseMetamodel();
            moduleClassLoader = setupClassLoader();
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
    protected ModuleLoaderContext createModuleLoaderContext(String name, String version, ModuleScope lookupScope) throws ModuleNotFoundException {
        return new JBossModuleLoaderContext(name, version, lookupScope);
    }
}

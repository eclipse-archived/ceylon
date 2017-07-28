package com.redhat.ceylon.module.loader.test;

import java.util.Map;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleGraph;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleNotFoundException;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.model.cmr.ModuleScope;
import com.redhat.ceylon.module.loader.BaseRuntimeModuleLoaderImpl;

public class TestableModuleLoader extends BaseRuntimeModuleLoaderImpl {

    public TestableModuleLoader(RepositoryManager repositoryManager,
            ClassLoader delegateClassLoader, Map<String, String> extraModules, boolean verbose) {
        super(repositoryManager, delegateClassLoader, extraModules, verbose);
    }

    
    public class TestableModuleLoaderContext extends RuntimeModuleLoaderContext {

        TestableModuleLoaderContext(String module, String version, ModuleScope lookupScope) throws ModuleNotFoundException {
            super(module, version, lookupScope);
        }

        @Override
        protected void initialise() throws ModuleNotFoundException {
            preloadModules();
        }

        @Override
        protected void initialiseMetamodel() {
            // don't do it
        }
        
        public String getModuleVersion(String name) {
            ModuleGraph.Module module = moduleGraph.findModule(name);
            return module != null ? module.version : null;
        }
    }

    @Override
    protected ModuleLoaderContext createModuleLoaderContext(String name, String version, ModuleScope lookupScope) throws ModuleNotFoundException {
        return new TestableModuleLoaderContext(name, version, lookupScope);
    }

    private TestableModuleLoaderContext getContext(){
        if(contexts.size() == 1){
            return (TestableModuleLoaderContext) contexts.values().iterator().next();
        }
        throw new RuntimeException("No context found");
    }
    
    public String getModuleVersion(String name) {
        TestableModuleLoaderContext context = getContext();
        return context.getModuleVersion(ModuleUtil.getModuleNameFromUri(name));
    }
}

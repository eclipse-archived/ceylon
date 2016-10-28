package com.redhat.ceylon.module.loader;

import java.util.Map;

import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.api.OverridesRuntimeResolver;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.loader.BaseModuleLoaderImpl;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleGraph;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleNotFoundException;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.ModuleScope;

public abstract class BaseRuntimeModuleLoaderImpl extends BaseModuleLoaderImpl {

    protected abstract class RuntimeModuleLoaderContext extends BaseModuleLoaderImpl.ModuleLoaderContext {
        
        protected RuntimeModuleLoaderContext(String module, String version, ModuleScope lookupScope) throws ModuleNotFoundException {
            super(module, version, lookupScope);
        }

        protected void initialiseMetamodel() {
            Overrides overrides = repositoryManager.getOverrides();
            // FIXME: apply Maven overrides to our overrides
            Metamodel.resetModuleManager(new OverridesRuntimeResolver(overrides));
            moduleGraph.visit(new ModuleGraph.Visitor(){
                @Override
                public void visit(ModuleGraph.Module module) {
                    registerInMetamodel(module);
                }
            });
        }

        private void registerInMetamodel(ModuleGraph.Module module) {
            // skip JDK modules
            if(jdkProvider.isJDKModule(module.name))
                return;
            // use the one we got from the CMR rather than the one for dependencies mapping
            ArtifactResult dependencyArtifact = module.artifact;
            // it may be optional, we already dealt with those checks earlier
            if(dependencyArtifact != null){
                ClassLoader dependencyClassLoader;
                if(module.inCurrentClassLoader)
                    dependencyClassLoader = delegateClassLoader;
                else
                    dependencyClassLoader = moduleClassLoader;
                registerInMetamodel(dependencyArtifact, dependencyClassLoader);
            }
        }
        
        private void registerInMetamodel(ArtifactResult artifact, ClassLoader classLoader) {
            if(verbose)
                log("Registering "+artifact.name()+"/"+artifact.version()+" in metamodel");
            Metamodel.loadModule(artifact.name(), artifact.version(), artifact, classLoader);
        }
        
    }
    
    public BaseRuntimeModuleLoaderImpl() {
        super(null, null);
    }
    
    public BaseRuntimeModuleLoaderImpl(RepositoryManager repoManager, ClassLoader delegateClassLoader) {
        super(repoManager, delegateClassLoader);
    }
    
    public BaseRuntimeModuleLoaderImpl(RepositoryManager repositoryManager, ClassLoader delegateClassLoader, 
            Map<String,String> extraModules, boolean verbose) {
        super(repositoryManager, delegateClassLoader, extraModules, verbose);
    }
    
}

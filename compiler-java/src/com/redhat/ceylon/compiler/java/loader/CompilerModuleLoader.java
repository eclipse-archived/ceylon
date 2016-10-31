package com.redhat.ceylon.compiler.java.loader;

import java.util.Map;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.DependencyOverride;
import com.redhat.ceylon.cmr.api.DependencyOverride.Type;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.loader.BaseModuleLoaderImpl;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleGraph;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleNotFoundException;
import com.redhat.ceylon.common.ModuleSpec;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.StatusPrinter;
import com.redhat.ceylon.compiler.java.tools.StatusPrinterAptProgressListener;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.Exclusion;
import com.redhat.ceylon.model.cmr.ModuleScope;

public class CompilerModuleLoader extends BaseModuleLoaderImpl {

    private StatusPrinter statusPrinter;

    public CompilerModuleLoader(RepositoryManager repositoryManager,
            ClassLoader delegateClassLoader, Map<String, String> extraModules, boolean verbose, StatusPrinter statusPrinter) {
        super(repositoryManager, delegateClassLoader, extraModules, verbose);
        this.statusPrinter = statusPrinter;
    }

    
    public class CompilerModuleLoaderContext extends ModuleLoaderContext {

        private StatusPrinterAptProgressListener progressListener;

        CompilerModuleLoaderContext(String module, String version, ModuleScope lookupScope) throws ModuleNotFoundException {
            super(module, version, lookupScope);
            if(statusPrinter != null){
                progressListener = new StatusPrinterAptProgressListener(statusPrinter){
                    @Override
                    protected long getNumberOfModulesResolved() {
                        return getModuleCount();
                    }
                };
            }
        }

        @Override
        protected void initialise() throws ModuleNotFoundException {
            preloadModules();
        }

        @Override
        protected void resolvingSuccess(ArtifactResult result) {
            if(progressListener != null)
                progressListener.retrievingModuleArtifactSuccess(toModuleSpec(result), result);
        }

        private ModuleSpec toModuleSpec(ArtifactResult result) {
            return new ModuleSpec(result.name(), result.version());
        }

        @Override
        protected void resolvingFailed(ArtifactContext artifactContext) {
            if(progressListener != null)
                progressListener.retrievingModuleArtifactFailed(toModuleSpec(artifactContext), artifactContext);
        }

        @Override
        protected void prepareContext(ArtifactContext artifactContext) {
            if(progressListener != null)
                progressListener.retrievingModuleArtifact(toModuleSpec(artifactContext), artifactContext);
        }
        
        private ModuleSpec toModuleSpec(ArtifactContext artifactContext) {
            return new ModuleSpec(artifactContext.getName(), artifactContext.getVersion());
        }

        public void fillOverrides(final Overrides overrides){
            moduleGraph.visit(new ModuleGraph.Visitor(){
                @Override
                public void visit(ModuleGraph.Module module) {
                    if(module.artifact != null){
                        // record the version
                        overrides.addSetArtifact(module.name, module.version);
                        // record Maven exclusions
                        for (ArtifactResult dep : module.artifact.dependencies()) {
                            if(!selectDependency(dep))
                                continue;
                            if(dep.getExclusions() != null){
                                for (Exclusion exclusion : dep.getExclusions()) {
                                    ArtifactContext artifactContext = Overrides.createMavenArtifactContext(exclusion.getGroupId(), exclusion.getArtifactId(), null, null, null);
                                    DependencyOverride doo = new DependencyOverride(artifactContext, Type.REMOVE, false, false);
                                    overrides.addRemovedArtifact(doo);
                                }
                            }
                        }
                    }
                }
            });
        }

        public String getModuleVersion(String name) {
            ModuleGraph.Module module = moduleGraph.findModule(name);
            return module != null ? module.version : null;
        }
    }

    @Override
    protected ModuleLoaderContext createModuleLoaderContext(String name, String version, ModuleScope lookupScope) throws ModuleNotFoundException {
        return new CompilerModuleLoaderContext(name, version, lookupScope);
    }

    private CompilerModuleLoaderContext getContext(){
        if(contexts.size() == 1){
            return (CompilerModuleLoaderContext) contexts.values().iterator().next();
        }
        throw new RuntimeException("No context found");
    }
    
    public String getModuleVersion(String name) {
        CompilerModuleLoaderContext context = getContext();
        return context.getModuleVersion(ModuleUtil.getModuleNameFromUri(name));
    }

    public void setupOverrides(Overrides overrides) {
        CompilerModuleLoaderContext context = getContext();
        context.fillOverrides(overrides);
    }

    public int getModuleCount() {
        CompilerModuleLoaderContext context = getContext();
        return context.getModuleCount();
    }
}

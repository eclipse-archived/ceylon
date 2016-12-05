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
import com.redhat.ceylon.compiler.java.tools.CeylonLog;
import com.redhat.ceylon.compiler.java.tools.StatusPrinterAptProgressListener;
import com.redhat.ceylon.langtools.tools.javac.util.Log.WriterKind;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.Exclusion;
import com.redhat.ceylon.model.cmr.ModuleScope;

public class CompilerModuleLoader extends BaseModuleLoaderImpl {

    private StatusPrinter statusPrinter;
    private CeylonLog log;

    public CompilerModuleLoader(RepositoryManager repositoryManager,
            ClassLoader delegateClassLoader, Map<String, String> extraModules, boolean verbose, 
            StatusPrinter statusPrinter, CeylonLog log) {
        super(repositoryManager, delegateClassLoader, extraModules, verbose);
        this.statusPrinter = statusPrinter;
        this.log = log;
    }

    @Override
    public void log(String string) {
        log.printRawLines(WriterKind.NOTICE, "["+string+"]");
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
            if(verbose)
                log("Pre-resolved module: "+result);
            if(progressListener != null)
                progressListener.retrievingModuleArtifactSuccess(toModuleSpec(result), result);
        }

        private ModuleSpec toModuleSpec(ArtifactResult result) {
            return new ModuleSpec(result.namespace(), result.name(), result.version());
        }

        @Override
        protected void resolvingFailed(ArtifactContext artifactContext) {
            if(verbose)
                log("Pre-resolving module failed for: "+artifactContext);
            if(progressListener != null)
                progressListener.retrievingModuleArtifactFailed(toModuleSpec(artifactContext), artifactContext);
        }

        @Override
        protected void prepareContext(ArtifactContext artifactContext) {
            if(verbose)
                log("Pre-resolving module: "+artifactContext);
            if(progressListener != null)
                progressListener.retrievingModuleArtifact(toModuleSpec(artifactContext), artifactContext);
        }
        
        private ModuleSpec toModuleSpec(ArtifactContext artifactContext) {
            return new ModuleSpec(artifactContext.getNamespace(), artifactContext.getName(), artifactContext.getVersion());
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

package com.redhat.ceylon.tools.moduleloading;

import java.io.IOException;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;

import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.loader.BaseModuleLoaderImpl;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleGraph;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleGraph.Module;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleGraph.Visitor;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleNotFoundException;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.ModuleScope;

public class ToolModuleLoader extends BaseModuleLoaderImpl {

    public class ToolModuleLoaderContext extends ModuleLoaderContext {

        protected ToolModuleLoaderContext(ModuleScope lookupScope) throws ModuleNotFoundException {
            super(null, null, lookupScope);
        }

        @Override
        protected void initialise() throws ModuleNotFoundException {
            // don't do anything, rely on loadModule calls
        }
        
        public boolean loadModule(String module, String version) throws ModuleNotFoundException{
            try {
                return loadModule(ModuleUtil.getNamespaceFromUri(module), 
                        ModuleUtil.getModuleNameFromUri(module), 
                        version, false, false, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void handleMissingModuleError(String name, String version) throws ModuleNotFoundException {
            // we don't throw, but collect errors
            tool.handleMissingModuleError(name, version);
        }
        
        public ArtifactResult getModuleArtifact(String name) {
            ModuleGraph.Module module = moduleGraph.findModule(name);
            return module != null ? module.artifact : null;
        }
        
        @Override
        protected boolean isExcluded(String name, String version) {
            return tool.shouldExclude(name, version);
        }
        
        @Override
        protected boolean includeOptional() {
            return tool.includeOptionalDependencies();
        }

        @Override
        public void cycleDetected(List<Module> path) {
            // pass it on
            tool.cycleDetected(path);
        }

        @Override
        protected boolean selectDependencies(String name, String version) {
            // Resolve even provided modules but not their dependencies since they're meaningless
            // because they can change on the container
            return !tool.isProvided(name, version);
        }

        @Override
        public boolean selectDependency(ArtifactResult dep) {
            if(!super.selectDependency(dep))
                return false;
            return !tool.skipDependency(dep);
        }

        public SortedMap<String, SortedSet<String>> getDuplicateModules() {
            return duplicateModules;
        }

        public void visitModules(Visitor visitor) {
            moduleGraph.visit(visitor);
        }

        public void resolve() throws ModuleNotFoundException {
            try {
                finishLoadingModules();
                Overrides overrides = repositoryManager.getOverrides();
                if(overrides == null){
                    overrides = Overrides.create();
                    repositoryManager.setOverrides(overrides);
                }
                fillOverrides(overrides);
                reloadArtifactResults();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ModuleLoadingTool tool;
    private ToolModuleLoaderContext context;

    public ToolModuleLoader(ModuleLoadingTool tool, RepositoryManager repoManager) {
        super(repoManager, null);
        this.tool = tool;
        try {
            this.context = new ToolModuleLoaderContext(ModuleScope.RUNTIME);
        } catch (ModuleNotFoundException e) {
            // this can't happen since we don't load modules
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClassLoader loadModule(String name, String version, ModuleScope lookupScope) throws ModuleNotFoundException {
        loadModuleForTool(name, version, lookupScope);
        return null;
    }

    public boolean loadModuleForTool(String name, String version, ModuleScope lookupScope) throws ModuleNotFoundException {
        return context.loadModule(name, version);
    }

    @Override
    protected ModuleLoaderContext createModuleLoaderContext(String name, String version, ModuleScope lookupScope) throws ModuleNotFoundException {
        // never called
        return new ToolModuleLoaderContext(lookupScope);
    }

    public ArtifactResult getModuleArtifact(String name) {
        return context.getModuleArtifact(ModuleUtil.getModuleNameFromUri(name));
    }

    public SortedMap<String, SortedSet<String>> getDuplicateModules() {
        return context.getDuplicateModules();
    }

    public void visitModules(ModuleGraph.Visitor visitor) {
        context.visitModules(visitor);
    }

    public void resolve() throws ModuleNotFoundException {
        context.resolve();
    }
}

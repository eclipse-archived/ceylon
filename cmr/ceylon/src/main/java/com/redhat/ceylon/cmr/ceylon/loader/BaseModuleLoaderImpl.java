package com.redhat.ceylon.cmr.ceylon.loader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.DependencyOverride;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.DependencyOverride.Type;
import com.redhat.ceylon.cmr.api.MavenArtifactContext;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleGraph.CycleListener;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleGraph.DependencySelector;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleGraph.Module;
import com.redhat.ceylon.cmr.impl.FlatRepository;
import com.redhat.ceylon.common.CeylonVersionComparator;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.Exclusion;
import com.redhat.ceylon.model.cmr.ModuleScope;
import com.redhat.ceylon.model.loader.JdkProvider;

public abstract class BaseModuleLoaderImpl implements ModuleLoader {
    protected final RepositoryManager repositoryManager;
    protected final ClassLoader delegateClassLoader;
    protected final JdkProvider jdkProvider;
    protected final Map<String, String> extraModules;
    
    protected final Map<String, ModuleLoaderContext> contexts = new HashMap<String, ModuleLoaderContext>();
    protected final boolean verbose;

    protected abstract class ModuleLoaderContext implements DependencySelector, CycleListener {
        protected final String module;
        protected final String modver;
        protected final ModuleScope lookupScope;
        
        protected final ModuleGraph moduleGraph = new ModuleGraph();
        
        protected ClassLoader moduleClassLoader;
        protected SortedMap<String, SortedSet<String>> duplicateModules = new TreeMap<>();
        
        protected ModuleLoaderContext(String module, String version, ModuleScope lookupScope) throws ModuleNotFoundException {
            this.module = module;
            this.modver = version;
            this.lookupScope = lookupScope;
        }

        protected abstract void initialise() throws ModuleNotFoundException;
        
        protected void preloadModules() throws ModuleNotFoundException{
            try {
                loadModule(ModuleUtil.getNamespaceFromUri(module), 
                        ModuleUtil.getModuleNameFromUri(module), 
                        modver, false, false, null);
                finishLoadingModules();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        protected void finishLoadingModules() throws IOException, ModuleNotFoundException {
            if(extraModules != null){
                for(Entry<String,String> entry : extraModules.entrySet()){
                    loadModule(ModuleUtil.getNamespaceFromUri(entry.getKey()), 
                            ModuleUtil.getModuleNameFromUri(entry.getKey()),
                            entry.getValue(), false, false, null);
                }
            }
            Overrides overrides = repositoryManager.getOverrides();
            if(overrides != null){
                for (ArtifactContext context : overrides.getAddedArtifacts()) {
                    loadModule(context.getNamespace(), 
                            context.getName(),
                            context.getVersion(), false, false, null);
                }
            }
            moduleGraph.pruneExclusions(this);
            moduleGraph.checkForCycles(this);
            if(verbose){
                moduleGraph.dump(false);
                System.err.println("Total: "+getModuleCount());
            }
        }

        /**
         * This is useful if you collected version overrides and plan to re-use the
         * ArtifactResult for their dependencies, which may be affected by the overrides.
         */
        protected void reloadArtifactResults(){
            moduleGraph.visit(new ModuleGraph.Visitor(){
                @Override
                public void visit(ModuleGraph.Module module) {
                    if(module.artifact != null){
                        ArtifactContext artifactContext = new ArtifactContext(module.artifact.namespace(), 
                                module.name, module.version, getArtifactSuffixes());
                        ArtifactResult result = repositoryManager.getArtifactResult(artifactContext);
                        module.artifact = result;
                    }
                }
            });
        }
        
        abstract protected String[] getArtifactSuffixes();

        public void fillOverrides(final Overrides overrides){
            moduleGraph.visit(new ModuleGraph.Visitor(){
                @Override
                public void visit(ModuleGraph.Module module) {
                    if(module.artifact != null){
                        // record the version
                        overrides.addSetArtifact(module.name, module.version);
                        if(verbose)
                            log("Fixing version of module "+module.name+" to "+module.version);
                        // record Maven exclusions
                        for (ArtifactResult dep : module.artifact.dependencies()) {
                            if(!selectDependency(dep))
                                continue;
                            if(dep.getExclusions() != null){
                                for (Exclusion exclusion : dep.getExclusions()) {
                                    ArtifactContext artifactContext = new MavenArtifactContext(exclusion.getGroupId(), exclusion.getArtifactId(), null, null, null);
                                    DependencyOverride doo = new DependencyOverride(artifactContext, Type.REMOVE, false, false);
                                    overrides.addRemovedArtifact(doo);
                                    if(verbose)
                                        log("Removing module "+exclusion.getGroupId()+":"+exclusion.getArtifactId());
                                }
                            }
                        }
                    }
                }
            });
        }

        public int getModuleCount() {
            return moduleGraph.getCount();
        }

        protected boolean loadModule(String namespace, String name, String version, boolean optional, boolean inCurrentClassLoader, ModuleGraph.Module dependent) 
        		throws IOException, ModuleNotFoundException  {
        	if(isExcluded(name, version))
        	    return false;
            ArtifactContext artifactContext = new ArtifactContext(namespace, name, version, getArtifactSuffixes());
            Overrides overrides = repositoryManager.getOverrides();
            if(overrides != null){
                if(overrides.isRemoved(artifactContext))
                    return false;
                ArtifactContext replacement = overrides.replace(artifactContext);
                if(replacement != null){
                    artifactContext = replacement;
                    name = replacement.getName();
                    version = replacement.getVersion();
                    namespace = replacement.getNamespace();
                }
                if(overrides.isVersionOverridden(artifactContext)){
                    version = overrides.getVersionOverride(artifactContext);
                    artifactContext.setVersion(version);
                }
            }
            // skip JDK modules
            if(jdkProvider.isJDKModule(name))
                return true; // consider them loaded
            ModuleGraph.Module loadedModule = moduleGraph.findModule(name);
            if(loadedModule != null){
                String loadedVersion = loadedModule.version;
                // we loaded the module already, but did we load it with the same version?
                if(!Objects.equals(version, loadedVersion)){
                    // remember the dupes
                    SortedSet<String> versions = duplicateModules.get(name);
                    if(versions == null){
                        versions = new TreeSet<>();
                        duplicateModules.put(name, versions);
                    }
                    versions.add(version);
                    // since we only come here on duplicates, there's a chance the loaded version wasn't saved
                    versions.add(loadedVersion);
                    // now select
                    if(CeylonVersionComparator.compareVersions(version, loadedModule.version) > 0){
                        // we want a newer version, keep going
                        if(verbose)
                            log("Replacing "+loadedModule+" with newer version "+version);
                    }else{
                        // we want an older version, just keep the one we have and ignore that
                        addDependency(dependent, loadedModule);
                        // already resolved and same version, we're good
                        return true;
                    }
                }else if(loadedModule.artifact == null){
                    // now we're sure the version was the same
                    // it was resolved to null so it was optional, but perhaps it's required now?
                    if(!optional){
                        throw new ModuleNotFoundException("Could not find module: "+ModuleUtil.makeModuleName(name, version));
                    }
                    addDependency(dependent, loadedModule);
                    // already resolved and same version, we're good
                    return true;
                }else{
                    addDependency(dependent, loadedModule);
                    // already resolved and same version, we're good
                    return true;
                }
            }
            if(verbose)
                log("Resolving "+name+"/"+version);
            prepareContext(artifactContext);
            ArtifactResult result = repositoryManager.getArtifactResult(artifactContext);
            if(!optional
                    && (result == null || result.artifact() == null || !result.artifact().exists())){
                resolvingFailed(artifactContext);
                // some tools want to throw, others to collect as many errors as possible
                handleMissingModuleError(name, version);
                // if it doesn't throw, treat it as a missing dependency like we do for optionals
            }else{
                resolvingSuccess(result);
            }
            // save even missing optional modules as nulls to not re-resolve them
            ModuleGraph.Module mod;
            if(dependent == null)
                mod = moduleGraph.addRoot(name, version);
            else
                mod = dependent.addDependency(name, version);
            if(loadedModule != null)
                loadedModule.replace(mod);
            mod.artifact = result;
            if(result != null && selectDependencies(name, version)){
                // everything we know should be in the current class loader
                // plus everything from flat repositories
                if(inCurrentClassLoader || result.repository() instanceof FlatRepository){
                    mod.inCurrentClassLoader = true;
                }
                for(ArtifactResult dep : result.dependencies()){
                    // stop if we get removed at any point
                    if(mod.replaced)
                        break;
                    if(!selectDependency(dep))
                        continue;
                    loadModule(dep.namespace(), dep.name(), dep.version(), dep.optional(), inCurrentClassLoader, mod);
                }
                if(name.equals(com.redhat.ceylon.model.typechecker.model.Module.DEFAULT_MODULE_NAME)){
                    // make sure we pull the language module for the default module which has no dependency
                    loadModule(namespace, com.redhat.ceylon.model.typechecker.model.Module.LANGUAGE_MODULE_NAME,
                            Versions.CEYLON_VERSION_NUMBER, false, inCurrentClassLoader, mod);
                }
            }
            return result != null;
        }

        @Override
        public void cycleDetected(List<Module> path) {
            // do nothing?
        }
        
        protected void handleMissingModuleError(String name, String version) throws ModuleNotFoundException {
            throw new ModuleNotFoundException("Could not find module: "+ModuleUtil.makeModuleName(name, version));
        }

        protected boolean selectDependencies(String name, String version) {
            return true;
        }

        protected boolean isExcluded(String name, String version) {
            return false;
        }

        protected void resolvingSuccess(ArtifactResult result) {
        }

        protected void resolvingFailed(ArtifactContext artifactContext) {
        }

        protected void prepareContext(ArtifactContext artifactContext) {
        }
        
        protected boolean includeOptional(){
            return false;
        }

        @Override
        public boolean selectDependency(ArtifactResult dep) {
            if(!includeOptional() && dep.optional())
                return false;
            return includeDependencyInLookupScope(dep);
        }
        
        @Override
        public boolean canExclude(Module mod) {
            // don't exclude our root
            if(mod.name.equals(module)
                    && mod.version.equals(modver))
                return false;
            // don't exclude extra modules
            if(extraModules != null){
                for(Entry<String,String> entry : extraModules.entrySet()){
                    if(mod.name.equals(entry.getKey())
                            && mod.version.equals(entry.getValue()))
                        return false;
                }
            }
            Overrides overrides = repositoryManager.getOverrides();
            if(overrides != null){
                for (ArtifactContext context : overrides.getAddedArtifacts()) {
                    if(mod.name.equals(context.getName())
                            && mod.version.equals(context.getVersion()))
                        return false;
                }
            }
            return true;
        }

        @SuppressWarnings("incomplete-switch")
        private boolean includeDependencyInLookupScope(ArtifactResult dep) {
            switch(lookupScope){
            case COMPILE:
                return dep.moduleScope() == ModuleScope.COMPILE;
            case RUNTIME:
                // I _think_ Maven test scopes are only for running tests locally: maven does not
                // produce test modules, or modules who when tested have additional deps
            case TEST:
                // FIXME: actually we may need two runtime scopes: CONTAINER and STANDALONE to
                // decide if we keep PROVIDED deps
                return dep.moduleScope() == ModuleScope.COMPILE
                    || dep.moduleScope() == ModuleScope.RUNTIME;
            }
            return false;
        }

        private void addDependency(ModuleGraph.Module from, ModuleGraph.Module to) {
            if(from != null)
                from.addDependency(to);
            else
                moduleGraph.addRoot(to);
        }
        
        public void cleanup() {
            if (moduleClassLoader != delegateClassLoader && moduleClassLoader instanceof URLClassLoader){
                try {
                    ((URLClassLoader) moduleClassLoader).close();
                } catch (IOException e) {
                    // ignore
                    e.printStackTrace();
                }
            }
            moduleGraph.clear();
            moduleClassLoader = null;
        }
        
        // For tests only
        public URL[] getClassLoaderURLs(){
            if (moduleClassLoader != delegateClassLoader && moduleClassLoader instanceof URLClassLoader) {
                return ((URLClassLoader) moduleClassLoader).getURLs();
            }
            return null;
        }
    }
    
    public BaseModuleLoaderImpl() {
        this(null, null);
    }
    
    public void log(String string) {
        System.err.println("[CMR:DEBUG] "+string);
    }

    public BaseModuleLoaderImpl(RepositoryManager repoManager, ClassLoader delegateClassLoader) {
        this(repoManager, delegateClassLoader, null, false);
    }
    
    public BaseModuleLoaderImpl(RepositoryManager repositoryManager, ClassLoader delegateClassLoader, 
            Map<String,String> extraModules, boolean verbose) {
        if (repositoryManager == null) {
            this.repositoryManager = CeylonUtils.repoManager().buildManager();
        } else {
            this.repositoryManager = repositoryManager;
        }
        if (delegateClassLoader == null) {
            this.delegateClassLoader = BaseModuleLoaderImpl.class.getClassLoader();
        } else {
            this.delegateClassLoader= delegateClassLoader;
        }
        this.verbose = verbose;
        // FIXME: support alternate JDKs in time
        this.jdkProvider = new JdkProvider();
        this.extraModules = extraModules;
    }
    
    @Override
    public ClassLoader loadModule(String name, String version) throws ModuleNotFoundException {
        return loadModule(name, version, ModuleScope.RUNTIME);
    }

    @Override
    public ClassLoader loadModule(String name, String version, ModuleScope lookupScope) throws ModuleNotFoundException {
        String key = name;
        ModuleLoaderContext ctx = contexts.get(key);
        if (ctx == null) {
            // we want a CL so it's runtime
            ctx = createModuleLoaderContext(name, version, lookupScope);
            ctx.initialise();
            contexts.put(key, ctx);
        }
        return ctx.moduleClassLoader;
    }

    protected abstract ModuleLoaderContext createModuleLoaderContext(String name, String version, ModuleScope lookupScope) throws ModuleNotFoundException;
    
    public void cleanup() {
        for (ModuleLoaderContext ctx : contexts.values()) {
            ctx.cleanup();
        }
        contexts.clear();
    }
    
    // For tests only
    public URL[] getClassLoaderURLs(String module){
        ModuleLoaderContext ctx = contexts.get(module);
        return ctx.getClassLoaderURLs();
    }
}

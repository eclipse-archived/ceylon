package com.redhat.ceylon.cmr.ceylon.loader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.MavenVersionComparator;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleGraph.DependencySelector;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleGraph.Module;
import com.redhat.ceylon.cmr.impl.FlatRepository;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.ModuleScope;
import com.redhat.ceylon.model.loader.JdkProvider;

public abstract class BaseModuleLoaderImpl implements ModuleLoader {
    protected final RepositoryManager repositoryManager;
    protected final ClassLoader delegateClassLoader;
    protected final JdkProvider jdkProvider;
    protected final Map<String, String> extraModules;
    
    protected final Map<String, ModuleLoaderContext> contexts = new HashMap<String, ModuleLoaderContext>();
    protected final boolean verbose;

    protected abstract class ModuleLoaderContext implements DependencySelector {
        protected final String module;
        protected final String modver;
        protected final ModuleScope lookupScope;
        
        protected final ModuleGraph moduleGraph = new ModuleGraph();
        
        protected ClassLoader moduleClassLoader;
        
        protected ModuleLoaderContext(String module, String version, ModuleScope lookupScope) throws ModuleNotFoundException {
            this.module = module;
            this.modver = version;
            this.lookupScope = lookupScope;
            initialise();
        }

        protected abstract void initialise() throws ModuleNotFoundException;
        
        protected void preloadModules() throws ModuleNotFoundException{
            try {
                loadModule(ModuleUtil.getNamespaceFromUri(module), 
                        ModuleUtil.getModuleNameFromUri(module), 
                        modver, false, false, null);
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
                        loadModule(ModuleUtil.getNamespaceFromUri(context.getName()), 
                                ModuleUtil.getModuleNameFromUri(context.getName()),
                                context.getVersion(), false, false, null);
                    }
                }
                moduleGraph.pruneExclusions(this);
                if(verbose){
                    moduleGraph.dump(false);
                    final int[] count = new int[]{0};
                    moduleGraph.visit(new ModuleGraph.Visitor(){
                        @Override
                        public void visit(ModuleGraph.Module module) {
                            if(module.artifact != null){
                                System.err.println(module);
                                count[0]++;
                            }
                        }
                    });
                    System.err.println("Total: "+count[0]);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        protected void loadModule(String namespace, String name, String version, boolean optional, boolean inCurrentClassLoader, ModuleGraph.Module dependent) 
        		throws IOException, ModuleNotFoundException  {
        	
            ArtifactContext artifactContext = new ArtifactContext(namespace, name, version, ArtifactContext.CAR, ArtifactContext.JAR);
            Overrides overrides = repositoryManager.getOverrides();
            if(overrides != null){
                if(overrides.isRemoved(artifactContext))
                    return;
                ArtifactContext replacement = overrides.replace(artifactContext);
                if(replacement != null){
                    artifactContext = replacement;
                    name = replacement.getName();
                    version = replacement.getVersion();
                }
                if(overrides.isVersionOverridden(artifactContext)){
                    version = overrides.getVersionOverride(artifactContext);
                    artifactContext.setVersion(version);
                }
            }
            // skip JDK modules
            if(jdkProvider.isJDKModule(name))
                return;
            ModuleGraph.Module loadedModule = moduleGraph.findModule(name);
            if(loadedModule != null){
                String loadedVersion = loadedModule.version;
                // we loaded the module already, but did we load it with the same version?
                if(!Objects.equals(version, loadedVersion)){
                    if(MavenVersionComparator.compareVersions(version, loadedModule.version) > 0){
                        // we want a newer version, keep going
                        if(verbose)
                            log("Replacing "+loadedModule+" with newer version "+version);
                    }else{
                        // we want an older version, just keep the one we have and ignore that
                        addDependency(dependent, loadedModule);
                        // already resolved and same version, we're good
                        return;
                    }
                }else if(loadedModule.artifact == null){
                    // now we're sure the version was the same
                    // it was resolved to null so it was optional, but perhaps it's required now?
                    if(!optional){
                        throw new ModuleNotFoundException("Could not find module: "+ModuleUtil.makeModuleName(name, version));
                    }
                    addDependency(dependent, loadedModule);
                    // already resolved and same version, we're good
                    return;
                }else{
                    addDependency(dependent, loadedModule);
                    // already resolved and same version, we're good
                    return;
                }
            }
            if(verbose)
                log("Resolving "+name+"/"+version);
            ArtifactResult result = repositoryManager.getArtifactResult(artifactContext);
            if(!optional
                    && (result == null || result.artifact() == null || !result.artifact().exists())){
                throw new ModuleNotFoundException("Could not find module: "+ModuleUtil.makeModuleName(name, version));
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
            if(result != null){
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
            }
        }

        @Override
        public boolean selectDependency(ArtifactResult dep) {
            // FIXME: make configurable
            if(dep.optional())
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
                // FIXME: actually we may need two runtime scopes: CONTAINER and STANDALONE to
                // decide if we keep PROVIDED deps
                return dep.moduleScope() == ModuleScope.COMPILE
                    || dep.moduleScope() == ModuleScope.RUNTIME;
            case TEST:
                return dep.moduleScope() == ModuleScope.COMPILE
                    || dep.moduleScope() == ModuleScope.RUNTIME
                    || dep.moduleScope() == ModuleScope.TEST;
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

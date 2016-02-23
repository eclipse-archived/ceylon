package com.redhat.ceylon.compiler.java.runtime.tools.impl;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.MavenVersionComparator;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.FlatRepository;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.model.OverridesRuntimeResolver;
import com.redhat.ceylon.compiler.java.runtime.tools.ModuleLoader;
import com.redhat.ceylon.compiler.java.runtime.tools.ModuleNotFoundException;
import com.redhat.ceylon.compiler.java.runtime.tools.impl.ModuleGraph.Module;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.ImportType;
import com.redhat.ceylon.model.cmr.JDKUtils;

public abstract class BaseModuleLoaderImpl implements ModuleLoader {
    final RepositoryManager repositoryManager;
    final ClassLoader delegateClassLoader;
    
    private Map<String, ModuleLoaderContext> contexts = new HashMap<String, ModuleLoaderContext>();
    protected boolean verbose;

    abstract class ModuleLoaderContext {
        final String module;
        final String modver;
        
        final ModuleGraph moduleGraph = new ModuleGraph();
        
        ClassLoader moduleClassLoader;
        
        ModuleLoaderContext(String module, String version) {
            this.module = module;
            this.modver = version;
            initialise();
        }

        abstract void initialise();
        
        void loadModule(String name, String version, boolean optional, boolean inCurrentClassLoader, ModuleGraph.Module dependent) throws IOException {
            ArtifactContext artifactContext = new ArtifactContext(name, version, ArtifactContext.CAR, ArtifactContext.JAR);
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
            if(JDKUtils.isJDKModule(name) || JDKUtils.isOracleJDKModule(name))
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
                    loadModule(dep.name(), dep.version(), dep.importType() == ImportType.OPTIONAL, inCurrentClassLoader, mod);
                }
            }
        }

        private void addDependency(Module from, Module to) {
            if(from != null)
                from.addDependency(to);
            else
                moduleGraph.addRoot(to);
        }

        protected void initialiseMetamodel() {
            Overrides overrides = repositoryManager.getOverrides();
            Metamodel.resetModuleManager(new OverridesRuntimeResolver(overrides));
            moduleGraph.visit(new ModuleGraph.Visitor(){
                @Override
                public void visit(Module module) {
                    registerInMetamodel(module);
                }
            });
        }

        private void registerInMetamodel(ModuleGraph.Module module) {
            // skip JDK modules
            if(JDKUtils.isJDKModule(module.name) || JDKUtils.isOracleJDKModule(module.name))
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
        this(null);
    }
    
    public void log(String string) {
        System.err.println("[CMR:DEBUG] "+string);
    }

    public BaseModuleLoaderImpl(ClassLoader delegateClassLoader) {
        this(CeylonUtils.repoManager().buildManager(), delegateClassLoader, false);
    }
    
    public BaseModuleLoaderImpl(RepositoryManager repositoryManager, ClassLoader delegateClassLoader, boolean verbose) {
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
    }
    
    @Override
    public ClassLoader loadModule(String name, String version) {
        if (contexts == null) {
            throw new ceylon.language.AssertionError("Cannot get load module after cleanup is called");
        }
        String key = name;
        ModuleLoaderContext ctx = contexts.get(key);
        if (ctx == null) {
            ctx = createModuleLoaderContext(name, version);
            contexts.put(key, ctx);
        }
        return ctx.moduleClassLoader;
    }
    
    abstract ModuleLoaderContext createModuleLoaderContext(String name, String version);
    
    public void cleanup() {
        if (contexts != null) {
            for (ModuleLoaderContext ctx : contexts.values()) {
                ctx.cleanup();
            }
            contexts = null;
        }
    }
    
    // For tests only
    public URL[] getClassLoaderURLs(String module){
        ModuleLoaderContext ctx = contexts.get(module);
        return ctx.getClassLoaderURLs();
    }
}

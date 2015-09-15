package com.redhat.ceylon.compiler.java.runtime.tools.impl;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.FlatRepository;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.tools.ModuleLoader;
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
        
        final Map<String, ArtifactResult> loadedModules = new HashMap<String, ArtifactResult>();
        final Map<String, String> loadedModuleVersions = new HashMap<String, String>();
        final Set<String> loadedModulesInCurrentClassLoader = new HashSet<String>();
        
        ClassLoader moduleClassLoader;
        
        ModuleLoaderContext(String module, String version) {
            this.module = module;
            this.modver = version;
            initialise();
        }

        abstract void initialise();
        
        void loadModule(String name, String version, boolean optional, boolean inCurrentClassLoader) throws IOException {
            ArtifactContext artifactContext = new ArtifactContext(name, version, ArtifactContext.CAR, ArtifactContext.JAR);
            Overrides overrides = repositoryManager.getOverrides();
            if(overrides != null){
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
            if(loadedModules.containsKey(name)){
                ArtifactResult loadedModule = loadedModules.get(name);
                String resolvedVersion = loadedModuleVersions.get(name);
                // we loaded the module already, but did we load it with the same version?
                if(!Objects.equals(version, resolvedVersion)){
                    // version conflict, even if one was a missing optional
                    throw new RuntimeException("Requiring two modules with the same name ("+name+") but conflicting versions: "+version+" and "+resolvedVersion);
                }
                // now we're sure the version was the same
                if(loadedModule == null){
                    // it was resolved to null so it was optional, but perhaps it's required now?
                    if(!optional){
                        throw new RuntimeException("Missing module: "+ModuleUtil.makeModuleName(name, version));
                    }
                }
                // already resolved and same version, we're good
                return;
            }
            ArtifactResult result = repositoryManager.getArtifactResult(artifactContext);
            if(!optional
                    && (result == null || result.artifact() == null || !result.artifact().exists())){
                throw new RuntimeException("Missing module: "+ModuleUtil.makeModuleName(name, version));
            }
            // save even missing optional modules as nulls to not re-resolve them
            loadedModules.put(name, result);
            loadedModuleVersions.put(name, version);
            if(result != null){
                // everything we know should be in the current class loader
                // plus everything from flat repositories
                if(inCurrentClassLoader || result.repository() instanceof FlatRepository){
                    loadedModulesInCurrentClassLoader.add(name);
                }
                for(ArtifactResult dep : result.dependencies()){
                    loadModule(dep.name(), dep.version(), dep.importType() == ImportType.OPTIONAL, inCurrentClassLoader);
                }
            }
        }

        // we only need the module name since we already dealt with conflicting versions
        void registerInMetamodel(String module, Set<String> registered) {
            if(!registered.add(module))
                return;
            // skip JDK modules
            if(JDKUtils.isJDKModule(module) || JDKUtils.isOracleJDKModule(module))
                return;
            // use the one we got from the CMR rather than the one for dependencies mapping
            ArtifactResult dependencyArtifact = loadedModules.get(module);
            // it may be optional, we already dealt with those checks earlier
            if(dependencyArtifact != null){
                ClassLoader dependencyClassLoader;
                if(loadedModulesInCurrentClassLoader.contains(module))
                    dependencyClassLoader = delegateClassLoader;
                else
                    dependencyClassLoader = moduleClassLoader;
                registerInMetamodel(dependencyArtifact, dependencyClassLoader, registered);
            }
        }
        
        private void registerInMetamodel(ArtifactResult artifact, ClassLoader classLoader, Set<String> registered) {
            Metamodel.loadModule(artifact.name(), artifact.version(), artifact, classLoader);
            // also register its dependencies
            for(ArtifactResult dep : artifact.dependencies()){
                registerInMetamodel(dep.name(), registered);
            }
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
            loadedModules.clear();
            loadedModuleVersions.clear();
            loadedModulesInCurrentClassLoader.clear();
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

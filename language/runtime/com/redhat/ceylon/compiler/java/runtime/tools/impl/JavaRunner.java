package com.redhat.ceylon.compiler.java.runtime.tools.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ImportType;
import com.redhat.ceylon.cmr.api.JDKUtils;
import com.redhat.ceylon.cmr.api.RepositoryException;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.FlatRepository;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.tools.JVMRuntimeOptions;
import com.redhat.ceylon.compiler.java.runtime.tools.Runner;
import com.redhat.ceylon.compiler.java.runtime.tools.RuntimeOptions;
import com.redhat.ceylon.compiler.typechecker.model.Module;

public class JavaRunner implements Runner {

    private Map<String,ArtifactResult> loadedModules = new HashMap<String,ArtifactResult>();
    private Map<String,String> loadedModuleVersions = new HashMap<String,String>();
    private RepositoryManager repositoryManager;
    private Set<String> loadedModulesInCurrentClassLoader = new HashSet<String>();
    private ClassLoader moduleClassLoader;
    private ClassLoader delegateClassLoader;
    private String module;
    private Map<String, String> extraModules;
    
    public JavaRunner(RuntimeOptions options, String module, String version){
        repositoryManager = CeylonUtils.repoManager()
                .userRepos(options.getUserRepositories())
                .systemRepo(options.getSystemRepository())
                .offline(options.isOffline())
                .buildManager();
        if(options instanceof JVMRuntimeOptions){
            delegateClassLoader = ((JVMRuntimeOptions) options).getDelegateClassLoader();
        }
        if(delegateClassLoader == null)
            delegateClassLoader = JavaRunner.class.getClassLoader();
        
        this.module = module;
        this.extraModules = options.getExtraModules();
        try {
            // those come from the delegate class loader
            loadModule(Module.LANGUAGE_MODULE_NAME, Versions.CEYLON_VERSION_NUMBER, false, true);
            loadModule("com.redhat.ceylon.compiler.java", Versions.CEYLON_VERSION_NUMBER, false, true);
            loadModule("com.redhat.ceylon.common", Versions.CEYLON_VERSION_NUMBER, false, true);
            loadModule("com.redhat.ceylon.module-resolver", Versions.CEYLON_VERSION_NUMBER, false, true);
            loadModule("com.redhat.ceylon.typechecker", Versions.CEYLON_VERSION_NUMBER, false, true);
            // these ones not necessarily
            loadModule(module, version, false, false);
            for(Entry<String,String> entry : options.getExtraModules().entrySet()){
                loadModule(entry.getKey(), entry.getValue(), false, false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(String... arguments){
        // make a Class loader for this module if required
        if(loadedModulesInCurrentClassLoader.contains(module))
            moduleClassLoader = delegateClassLoader;
        else
            moduleClassLoader = makeModuleClassLoader();
        // initialise the metamodel
        registerInMetamodel("ceylon.language");
        registerInMetamodel("com.redhat.ceylon.typechecker");
        registerInMetamodel("com.redhat.ceylon.common");
        registerInMetamodel("com.redhat.ceylon.module-resolver");
        registerInMetamodel("com.redhat.ceylon.compiler.java");
        registerInMetamodel(module);
        if(extraModules != null){
            for(String extraModule : extraModules.keySet()){
                registerInMetamodel(extraModule);
            }
        }
        // now load and invoke the main class
        invokeMain(module, arguments);
    }
    
    @Override
    public void cleanup() {
        if(moduleClassLoader != delegateClassLoader
                && moduleClassLoader instanceof URLClassLoader){
            try {
                ((URLClassLoader) moduleClassLoader).close();
            } catch (IOException e) {
                // ignore
                e.printStackTrace();
            }
        }
    }
    
    // for tests
    public URL[] getClassLoaderURLs(){
        if(moduleClassLoader != delegateClassLoader
                && moduleClassLoader instanceof URLClassLoader){
            return ((URLClassLoader) moduleClassLoader).getURLs();
        }
        return null;
    }
    
    private void invokeMain(String module, String[] arguments) {
        String className;
        if(module.equals(com.redhat.ceylon.compiler.typechecker.model.Module.DEFAULT_MODULE_NAME))
            className = "run_";
        else
            className = module + ".run_";
        try {
            Class<?> runClass = moduleClassLoader.loadClass(className);
            Method main = runClass.getMethod("main", String[].class);
            Thread currentThread = Thread.currentThread();
            ClassLoader oldCcl = currentThread.getContextClassLoader();
            try{
                currentThread.setContextClassLoader(moduleClassLoader);
                main.invoke(null, (Object)arguments);
            }finally{
                currentThread.setContextClassLoader(oldCcl);
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find main class for module "+module+": "+className, e);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException 
                | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("Failed to invoke main method for module "+module+": "+className, e);
        }
    }

    private ClassLoader makeModuleClassLoader() {
        // we need to make a class loader for all the modules it requires which are not provided by the current class loader
        Set<String> modulesNotInCurrentClassLoader = new HashSet<String>();
        for(Entry<String, ArtifactResult> entry : loadedModules.entrySet()){
            if(entry.getValue() != null)
                modulesNotInCurrentClassLoader.add(entry.getKey());
        }
        modulesNotInCurrentClassLoader.removeAll(loadedModulesInCurrentClassLoader);
        URL[] urls = new URL[modulesNotInCurrentClassLoader.size()];
        int i=0;
        for(String module : modulesNotInCurrentClassLoader){
            ArtifactResult artifact = loadedModules.get(module);
            try {
                @SuppressWarnings("deprecation")
                URL url = artifact.artifact().toURL();
                urls[i++] = url;
            } catch (MalformedURLException | RepositoryException e) {
                throw new RuntimeException("Failed to get a URL for module file for "+module, e);
            }
        }
        return new URLClassLoader(urls , delegateClassLoader);
    }

    private void loadModule(String name, String version, boolean optional, boolean inCurrentClassLoader) throws IOException {
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
        ArtifactContext artifactContext = new ArtifactContext(name, version, ArtifactContext.CAR, ArtifactContext.JAR);
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
    private void registerInMetamodel(String module) {
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
            registerInMetamodel(dependencyArtifact, dependencyClassLoader);
        }
    }
    
    private void registerInMetamodel(ArtifactResult artifact, ClassLoader classLoader) {
        Metamodel.loadModule(artifact.name(), artifact.version(), artifact, classLoader);
        // also register its dependencies
        for(ArtifactResult dep : artifact.dependencies()){
            registerInMetamodel(dep.name());
        }
    }

}

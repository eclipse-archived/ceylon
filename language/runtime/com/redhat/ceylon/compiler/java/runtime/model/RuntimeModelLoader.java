package com.redhat.ceylon.compiler.java.runtime.model;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.modules.ModuleClassLoader;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.JDKUtils;
import com.redhat.ceylon.cmr.impl.JULLogger;
import com.redhat.ceylon.common.runtime.CeylonModuleClassLoader;
import com.redhat.ceylon.compiler.loader.ModelResolutionException;
import com.redhat.ceylon.compiler.loader.impl.reflect.CachedTOCJars;
import com.redhat.ceylon.compiler.loader.impl.reflect.ReflectionModelLoader;
import com.redhat.ceylon.compiler.loader.impl.reflect.mirror.ReflectionClass;
import com.redhat.ceylon.compiler.loader.impl.reflect.mirror.ReflectionUtils;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.loader.model.LazyModule;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Modules;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Unit;

public class RuntimeModelLoader extends ReflectionModelLoader {

    public static final int MAX_JBOSS_MODULES_WAITS = 1;
    
    private Map<Module,ClassLoader> classLoaders = new HashMap<Module,ClassLoader>();
    private Map<String, Module> moduleCache = new HashMap<String, Module>();
    private CachedTOCJars jars = new CachedTOCJars();

    public RuntimeModelLoader(ModuleManager moduleManager, Modules modules) {
        super(moduleManager, modules, new JULLogger());
    }

    @Override
    public void loadStandardModules() {
        // set up the type factory and that's it: do not try to load the language module package before it's set up
        // by Metamodel.loadModule
        Module languageModule = findOrCreateModule(CEYLON_LANGUAGE, null);
        addModuleToClassPath(languageModule, null);
        Package languagePackage = findOrCreatePackage(languageModule, CEYLON_LANGUAGE);
        typeFactory.setPackage(languagePackage);
        
        // make sure the jdk modules are loaded because those are not initialised by jboss modules nor the IDE Launcher
        for(String jdkModule : JDKUtils.getJDKModuleNames())
            findOrCreateModule(jdkModule, JDKUtils.jdk.version);
        for(String jdkOracleModule : JDKUtils.getOracleJDKModuleNames())
            findOrCreateModule(jdkOracleModule, JDKUtils.jdk.version);
    }
    
    @Override
    protected List<String> getPackageList(Module module, String packageName) {
        return jars.getPackageList(packageName);
    }

    @Override
    protected boolean packageExists(Module module, String packageName) {
        return jars.packageExists(packageName);
    }

    @Override
    protected Class<?> loadClass(Module module, String name) {
        ClassLoader classLoader = classLoaders.get(module);
        if(classLoader == null){
            if(JDKUtils.isJDKModule(module.getNameAsString())
                    || JDKUtils.isOracleJDKModule(module.getNameAsString())){
                // the JDK does not have class loaders so load it from the root class loader
                try {
                    return ClassLoader.getSystemClassLoader().loadClass(name);
                } catch (ClassNotFoundException e) {
                    return null;
                }
            }
            return null;
        }
        try{
            return classLoader.loadClass(name);
        }catch(ClassNotFoundException x){
            return null;
        }
    }

    @Override
    public void addModuleToClassPath(Module module, ArtifactResult artifact) {
        String cacheKey = cacheKeyByModule(module.getNameAsString(), module.getVersion());
        moduleCache.put(cacheKey, module);
        if(artifact == null)
            return;
        File file = artifact.artifact();
        if(file == null)
            return;
        jars.addJar(file);
        if(module instanceof LazyModule){
            ((LazyModule) module).loadPackageList(artifact);
        }
    }

    public void addModuleClassLoader(Module module, ClassLoader classLoader) {
        classLoaders.put(module, classLoader);
    }

    @Override
    protected Module findModuleForClassMirror(ClassMirror classMirror) {
        Class<?> klass = ((ReflectionClass)classMirror).klass;
        Module ret = findModuleForClass(klass);
        if(ret == null)
            throw new ModelResolutionException("Could not find module for class "+klass);
        return ret;
    }

    public Module findModuleForClass(Class<?> klass){
        ClassLoader cl = klass.getClassLoader();
        if(cl instanceof ModuleClassLoader){
            ModuleClassLoader classLoader = (ModuleClassLoader)cl;
            org.jboss.modules.Module jbossModule = classLoader.getModule();
            String name = jbossModule.getIdentifier().getName();
            String version = jbossModule.getIdentifier().getSlot();
            String cacheKey = cacheKeyByModule(name, version);
            Module ret = moduleCache.get(cacheKey);
            if(ret == null){
                // there's a good chance we didn't get the module loaded in time, wait until that classloader is
                // registered then, but give it a nudge:

                if(cl instanceof CeylonModuleClassLoader){
                    // this can complete in another thread or this thread
                    ((CeylonModuleClassLoader) cl).registerInMetaModel();
                }

                Object lock = getLock();
                synchronized(lock){
                    int tries = MAX_JBOSS_MODULES_WAITS;
                    while(!classLoaders.containsValue(cl)){
                        try {
                            lock.wait(5000);
                        } catch (InterruptedException e) {
                            throw new ModelResolutionException(e);
                        }
                        if(tries-- < 0)
                            throw new ModelResolutionException("Failed to find registered classloader for "+klass);
                    }
                    ret = moduleCache.get(cacheKey);
                }
            }
            return ret;
        }else{
            // revert to a single classloader version?
            // FIXME: perhaps we can have other one-classloader-to-one-module setups than jboss modules?
            String pkgName = ReflectionUtils.getPackageName(klass);
            // even on jboss modules, the jdk has no class loader, so jdk classes will have to be resolved
            // to the jdk modules by package
            String jdkModuleName = JDKUtils.getJDKModuleNameForPackage(pkgName);
            if(jdkModuleName != null)
                return findModule(jdkModuleName, JDKUtils.jdk.version);
            return lookupModuleByPackageName(pkgName);
        }
    }
    
    @Override
    protected String cacheKeyByModule(Module module, String name) {
        if(module == null)
            throw new NullPointerException("No module found for "+name);
        return super.cacheKeyByModule(module, name);
    }
    
    private String cacheKeyByModule(String name, String version) {
        if(name.equals(Module.DEFAULT_MODULE_NAME))
            return name + "/"; // no version
        return name + "/" + version;
    }

    public Unit getUnit() {
        return typeFactory;
    }
    
    @Override
    protected Runnable makeModelErrorReporter(Module module, final String message) {
        return makeModelErrorReporter(message);
    }

    @Override
    protected Runnable makeModelErrorReporter(final String message) {
        return new Runnable(){
            @Override
            public void run() {
                throw new ModelResolutionException(message);
            }
        };
    }
}

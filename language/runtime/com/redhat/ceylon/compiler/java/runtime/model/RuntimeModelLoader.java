package com.redhat.ceylon.compiler.java.runtime.model;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.modules.ModuleClassLoader;

import com.redhat.ceylon.common.runtime.CeylonModuleClassLoader;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.JDKUtils;
import com.redhat.ceylon.model.loader.LoaderJULLogger;
import com.redhat.ceylon.model.loader.ModelResolutionException;
import com.redhat.ceylon.model.loader.impl.reflect.CachedTOCJars;
import com.redhat.ceylon.model.loader.impl.reflect.ReflectionModelLoader;
import com.redhat.ceylon.model.loader.impl.reflect.mirror.ReflectionClass;
import com.redhat.ceylon.model.loader.impl.reflect.mirror.ReflectionUtils;
import com.redhat.ceylon.model.loader.mirror.ClassMirror;
import com.redhat.ceylon.model.loader.mirror.MethodMirror;
import com.redhat.ceylon.model.loader.model.AnnotationProxyClass;
import com.redhat.ceylon.model.loader.model.AnnotationProxyMethod;
import com.redhat.ceylon.model.loader.model.LazyFunction;
import com.redhat.ceylon.model.loader.model.LazyModule;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Modules;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.UnknownType.ErrorReporter;
import com.redhat.ceylon.model.typechecker.util.ModuleManager;

public class RuntimeModelLoader extends ReflectionModelLoader {

    public static final int MAX_JBOSS_MODULES_WAITS = 4;
    public static final int JBOSS_MODULES_TIMEOUT = 5000;
    
    private Map<Module,ClassLoader> classLoaders = new HashMap<Module,ClassLoader>();
    private Map<String, Module> moduleCache = new HashMap<String, Module>();
    private CachedTOCJars jars = new CachedTOCJars();

    public RuntimeModelLoader(ModuleManager moduleManager, Modules modules) {
        super(moduleManager, modules, new LoaderJULLogger());
    }

    @Override
    public void loadStandardModules() {
        // set up the type factory and that's it: do not try to load the language module package before it's set up
        // by Metamodel.loadModule
        Module languageModule = findOrCreateModule(CEYLON_LANGUAGE, null);
        addModuleToClassPath(languageModule, (ArtifactResult)null);
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
        return jars.getPackageList(module, packageName);
    }

    @Override
    protected boolean packageExists(Module module, String packageName) {
        return jars.packageExists(module, packageName);
    }

    public byte[] getContents(String path) {
        return jars.getContents(path);
    }

    public URI getContentUri(String path) {
        return jars.getContentUri(path);
    }

    public byte[] getContents(Module module, String path) {
        return jars.getContents(module, path);
    }
    
    public URI getContentUri(Module module, String path) {
        return jars.getContentUri(module, path);
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
        }catch(ClassNotFoundException|NoClassDefFoundError x){
            return null;
        }
    }

    @Override
    public void addModuleToClassPath(final Module module, ArtifactResult artifact) {
        String cacheKey = cacheKeyByModule(module.getNameAsString(), module.getVersion());
        moduleCache.put(cacheKey, module);
        if(artifact == null)
            return;
        jars.addJar(artifact, module);
        if(module instanceof LazyModule){
            ((LazyModule) module).setPackagePathsProvider(new LazyModule.PackagePathsProvider() {
				@Override
				public Set<String> getPackagePaths() {
					return jars.getPackagePaths(module);
				}
			});
        }
    }

    public void addModuleClassLoader(Module module, ClassLoader classLoader) {
        classLoaders.put(module, classLoader);
    }

    @Override
    public Module findModuleForClassMirror(ClassMirror classMirror) {
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
                            lock.wait(JBOSS_MODULES_TIMEOUT);
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
    protected ErrorReporter makeModelErrorReporter(Module module, final String message) {
        return makeModelErrorReporter(message);
    }

    @Override
    protected ErrorReporter makeModelErrorReporter(final String message) {
        return new ErrorReporter(message){
            @Override
            public void reportError() {
                throw new ModelResolutionException(message);
            }
        };
    }

    @Override
    protected void setAnnotationConstructor(LazyFunction method, MethodMirror meth) {
        // no code needed
    }

    @Override
    protected void makeInteropAnnotationConstructorInvocation(AnnotationProxyMethod ctor, AnnotationProxyClass klass, List<Parameter> ctorParams) {
        // no code needed
    }
}

package com.redhat.ceylon.compiler.java.runtime.model;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.modules.ModuleClassLoader;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.impl.JULLogger;
import com.redhat.ceylon.compiler.loader.impl.reflect.CachedTOCJars;
import com.redhat.ceylon.compiler.loader.impl.reflect.ReflectionModelLoader;
import com.redhat.ceylon.compiler.loader.impl.reflect.mirror.ReflectionClass;
import com.redhat.ceylon.compiler.loader.impl.reflect.mirror.ReflectionUtils;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.loader.model.LazyModule;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Modules;

public class RuntimeModelLoader extends ReflectionModelLoader {

    private Map<Module,ClassLoader> classLoaders = new HashMap<Module,ClassLoader>();
    private Map<String, Module> moduleCache = new HashMap<String, Module>();
    private CachedTOCJars jars = new CachedTOCJars();

    public RuntimeModelLoader(ModuleManager moduleManager, Modules modules) {
        super(moduleManager, modules, new JULLogger());
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
        if(classLoader == null)
            return null;
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
        return findModuleForClass(klass);
    }

    public Module findModuleForClass(Class<?> klass){
        ClassLoader cl = klass.getClassLoader();
        if(cl instanceof ModuleClassLoader){
            ModuleClassLoader classLoader = (ModuleClassLoader)cl;
            org.jboss.modules.Module jbossModule = classLoader.getModule();
            String name = jbossModule.getIdentifier().getName();
            String version = jbossModule.getIdentifier().getSlot();
            String cacheKey = cacheKeyByModule(name, version);
            return moduleCache.get(cacheKey);
        }else{
            // revert to a single classloader version?
            // FIXME: perhaps we can have other one-classloader-to-one-module setups than jboss modules?
            String pkgName = ReflectionUtils.getPackageName(klass);
            return lookupModuleInternal(pkgName);
        }
    }
    
    private String cacheKeyByModule(String name, String version) {
        if(name.equals(Module.DEFAULT_MODULE_NAME))
            return name + "/"; // no version
        return name + "/" + version;
    }

}

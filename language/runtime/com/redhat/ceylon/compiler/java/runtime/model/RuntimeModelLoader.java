package com.redhat.ceylon.compiler.java.runtime.model;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.impl.JULLogger;
import com.redhat.ceylon.compiler.loader.impl.reflect.CachedTOCJars;
import com.redhat.ceylon.compiler.loader.impl.reflect.ReflectionModelLoader;
import com.redhat.ceylon.compiler.loader.model.LazyModule;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Modules;

public class RuntimeModelLoader extends ReflectionModelLoader {

    private Map<Module,ClassLoader> classLoaders = new HashMap<Module,ClassLoader>();
    private CachedTOCJars jars = new CachedTOCJars();

    public RuntimeModelLoader(ModuleManager moduleManager, Modules modules) {
        super(moduleManager, modules, new JULLogger());
    }

    @Override
    protected List<String> getPackageList(String packageName) {
        return jars.getPackageList(packageName);
    }

    @Override
    protected boolean packageExists(String packageName) {
        return jars.packageExists(packageName);
    }

    @Override
    protected Class<?> loadClass(String name) {
        for(ClassLoader cl : classLoaders.values()){
            try{
                return cl.loadClass(name);
            }catch(ClassNotFoundException x){
                // keep trying
            }
        }
        return null;
//        // try the system class loader
//        try {
//            return ClassLoader.getSystemClassLoader().loadClass(name);
//        } catch (ClassNotFoundException e) {
//            return null;
//        }
    }

    @Override
    public void addModuleToClassPath(Module module, ArtifactResult artifact) {
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

}

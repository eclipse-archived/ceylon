package com.redhat.ceylon.compiler.reflectionmodelloader;

import java.net.URLClassLoader;

import com.redhat.ceylon.compiler.modelloader.AbstractModelLoader;
import com.redhat.ceylon.compiler.modelloader.LazyModule;

public class ReflectionModule extends LazyModule {

    private ReflectionModuleManager modelManager;
    private ClassLoader classLoader;

    public ReflectionModule(ReflectionModuleManager reflectionModuleManager) {
        this.modelManager = reflectionModuleManager;
    }

    @Override
    protected AbstractModelLoader getModelLoader() {
        return modelManager.getModelLoader();
    }

    public void setClassLoader(URLClassLoader cl) {
        if(this.classLoader != null){
            throw new RuntimeException("Module already has a classloader: "+getNameAsString());
        }
        this.classLoader = cl;
    }
    
    public ClassLoader getClassLoader(){
        return classLoader;
    }

}

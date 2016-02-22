package com.redhat.ceylon.model.loader.impl.reflect.model;

import java.util.List;

import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.loader.model.LazyModule;
import com.redhat.ceylon.model.typechecker.model.Package;

public class ReflectionModule extends LazyModule {

    private ReflectionModuleManager modelManager;
    private boolean packagesLoaded = false;

    public ReflectionModule(ReflectionModuleManager reflectionModuleManager) {
        this.modelManager = reflectionModuleManager;
    }

    @Override
    protected AbstractModelLoader getModelLoader() {
        return modelManager.getModelLoader();
    }
    
    @Override
    public List<Package> getPackages() {
        // make sure we're complete
        AbstractModelLoader modelLoader = getModelLoader();
        if(!packagesLoaded){
            synchronized(modelLoader.getLock()){
                if(!packagesLoaded){
                    String name = getNameAsString();
                    for(String pkg : getJarPackages()){
                        // special case for the language module to hide stuff
                        if(!name.equals(AbstractModelLoader.CEYLON_LANGUAGE) || pkg.startsWith(AbstractModelLoader.CEYLON_LANGUAGE))
                            modelLoader.findOrCreatePackage(this, pkg);
                    }
                    packagesLoaded = true;
                }
            }
        }
        return super.getPackages();
    }
}

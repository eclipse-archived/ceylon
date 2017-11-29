/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.loader.impl.reflect.model;

import java.util.List;

import org.eclipse.ceylon.model.loader.AbstractModelLoader;
import org.eclipse.ceylon.model.loader.model.LazyModule;
import org.eclipse.ceylon.model.typechecker.model.Package;

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
        final AbstractModelLoader modelLoader = getModelLoader();
        if(!packagesLoaded){
            modelLoader.synchronizedRun(new Runnable() {
                @Override
                public void run() {
                    if(!packagesLoaded){
                        String name = getNameAsString();
                        for(String pkg : getJarPackages()){
                            // special case for the language module to hide stuff
                            if(!name.equals(AbstractModelLoader.CEYLON_LANGUAGE) || pkg.startsWith(AbstractModelLoader.CEYLON_LANGUAGE))
                                modelLoader.findOrCreatePackage(ReflectionModule.this, pkg);
                        }
                        packagesLoaded = true;
                    }
                }
            });
        }
        return super.getPackages();
    }
}

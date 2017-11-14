/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.module.loader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.ceylon.loader.ModuleGraph;
import org.eclipse.ceylon.cmr.ceylon.loader.ModuleNotFoundException;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.ModuleScope;
import org.eclipse.ceylon.model.cmr.RepositoryException;

public class FlatpathModuleLoader extends BaseRuntimeModuleLoaderImpl {

    public FlatpathModuleLoader() {
        this(null, null);
    }

    /*
     * Used by reflection in org.eclipse.ceylon.common.tool.ToolLoader
     */
    public FlatpathModuleLoader(RepositoryManager repoManager, ClassLoader delegateClassLoader) {
        this(repoManager, delegateClassLoader, null, false);
    }

    public FlatpathModuleLoader(RepositoryManager repositoryManager,
            ClassLoader delegateClassLoader, Map<String, String> extraModules, boolean verbose) {
        super(repositoryManager, delegateClassLoader, extraModules, verbose);
    }

    class FlatpathModuleLoaderContext extends RuntimeModuleLoaderContext {

        FlatpathModuleLoaderContext(String module, String version, ModuleScope lookupScope) throws ModuleNotFoundException {
            super(module, version, lookupScope);
        }

        @Override
        protected void initialise() throws ModuleNotFoundException {
            preloadModules();
            moduleClassLoader = setupClassLoader();
            initialiseMetamodel();
        }

        private ClassLoader setupClassLoader() {
            // make a Class loader for this module if required
            ModuleGraph.Module loadedModule = moduleGraph.findModule(module);
            if(loadedModule.inCurrentClassLoader)
                return delegateClassLoader;
            else
                return makeModuleClassLoader();
        }
        
        private ClassLoader makeModuleClassLoader() {
            final Set<ModuleGraph.Module> modulesNotInCurrentClassLoader = new HashSet<ModuleGraph.Module>();
            // we need to make a class loader for all the modules it requires which are not provided by the current class loader
            moduleGraph.visit(new ModuleGraph.Visitor(){
                @Override
                public void visit(ModuleGraph.Module module) {
                    if(!module.inCurrentClassLoader && module.artifact != null)
                        modulesNotInCurrentClassLoader.add(module);
                }
            });
            URL[] urls = new URL[modulesNotInCurrentClassLoader.size()];
            if(verbose)
                log("Making classpath with "+urls.length+" jars");
            int i=0;
            for(ModuleGraph.Module module : modulesNotInCurrentClassLoader){
                ArtifactResult artifact = module.artifact;
                try {
                    @SuppressWarnings("deprecation")
                    URL url = artifact.artifact().toURL();
                    if(verbose)
                        log(" cp["+i+"] = "+url);
                    urls[i++] = url;
                } catch (MalformedURLException | RepositoryException e) {
                    throw new RuntimeException("Failed to get a URL for module file for "+module, e);
                }
            }
            return new URLClassLoader(urls , delegateClassLoader);
        }
    }

    @Override
    protected ModuleLoaderContext createModuleLoaderContext(String name, String version, ModuleScope lookupScope) throws ModuleNotFoundException {
        return new FlatpathModuleLoaderContext(name, version, lookupScope);
    }
}

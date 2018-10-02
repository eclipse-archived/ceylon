/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.loader.model;

import java.util.Arrays;
import java.util.List;

import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.common.Backends;
import org.eclipse.ceylon.model.cmr.JDKUtils;
import org.eclipse.ceylon.model.loader.AbstractModelLoader;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.ModuleImport;
import org.eclipse.ceylon.model.typechecker.util.ModuleManager;

/**
 * ModuleManager which can load artifacts from jars and cars.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public abstract class LazyModuleManager extends ModuleManager {

    public LazyModuleManager() {
        super();
    }

    @Override
    public Module getOrCreateModule(List<String> moduleName, String version) {
        String name = ModelUtil.formatPath(moduleName);
        if(JDKUtils.isPreJava9ModuleNameOnJava9(name, version))
            moduleName = splitModuleName(JDKUtils.getJava9ModuleName(name, version));
        return super.getOrCreateModule(moduleName, version);
    }
    
    protected void setupIfJDKModule(LazyModule module) {
        // Make sure that the java modules are set up properly.
        // Bad jdk versions will not be made available and the module validator
        // will fail to load their artifacts, and the error is properly handled by the lazy module manager in
        // attachErrorToDependencyDeclaration()
        String nameAsString = module.getNameAsString();
        String version = module.getVersion();
        if(version != null
                // We can't use the model loader at this point since it has not been initialised yet,
                // and it doesn't matter which JdkProvider we use as JDK modules still need that fix
                && (JDKUtils.isJDKModule(nameAsString) || JDKUtils.isOracleJDKModule(nameAsString)
                        || JDKUtils.isPreJava9ModuleNameOnJava9(nameAsString, version))){
            if(JDKUtils.jdk.providesVersion(version)){
                module.setAvailable(true);
                module.setJava(true);
                module.setNativeBackends(Backend.Java.asSet());
            }
        }
    }


    /**
     * To be overriden by reflection module manager, because reflection requires even types of private members
     * of imported modules to be in the classpath, and those could be of unimported modules (since they're private
     * that's allowed).
     */
    public boolean shouldLoadTransitiveDependencies(){
        return false;
    }
    
    @Override
    protected abstract Module createModule(List<String> moduleName, String version);
    
    public abstract AbstractModelLoader getModelLoader();

    /**
     * Return true if this module should be loaded from source we are compiling
     * and not from its compiled artifact at all. Returns false by default, so
     * modules will be laoded from their compiled artifact.
     */
    public boolean isModuleLoadedFromSource(String moduleName){
        return false;
    }
    
    @Override
    public Iterable<String> getSearchedArtifactExtensions() {
        return Arrays.asList("car", "jar");
    }

    @Override
    public Backends getSupportedBackends() {
        return Backend.Java.asSet();
    }
    
    @Override
    public void addImplicitImports() {
        Module languageModule = modules.getLanguageModule();
        for(Module m : modules.getListOfModules()){
            // Java modules don't depend on ceylon.language
            if((m instanceof LazyModule == false || !((LazyModule)m).isJava()) && !m.equals(languageModule)) {
                // add ceylon.language if required
                ModuleImport moduleImport = findImport(m, languageModule);
                if (moduleImport == null) {
                    moduleImport = new ModuleImport(null, languageModule, false, true);
                    m.addImport(moduleImport);
                }
            }
        }
    }
    
    @Override
    protected boolean compareVersions(Module current, String version, String currentVersion) {
        String name = current.getNameAsString();
        // We can't use the jdk provider at this point since it has not been initialised yet,
        // and it doesn't matter which JdkProvider we use as JDK modules still need that fix
        if(JDKUtils.isJDKModule(name) || JDKUtils.isOracleJDKModule(name)){
            // if we're running JDK8, pretend that it provides JDK7 modules
            if(JDKUtils.jdk.providesVersion(version)
                    && JDKUtils.jdk.providesVersion(currentVersion))
                return true;
        }
        return currentVersion == null || version == null || currentVersion.equals(version);
    }
}

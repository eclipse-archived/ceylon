/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.model;

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ceylon.common.AndroidUtil;
import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.RuntimeResolver;
import org.eclipse.ceylon.model.loader.AbstractModelLoader;
import org.eclipse.ceylon.model.loader.JvmBackendUtil;
import org.eclipse.ceylon.model.loader.StaticMetamodelLoader;
import org.eclipse.ceylon.model.loader.impl.reflect.model.ReflectionModule;
import org.eclipse.ceylon.model.loader.impl.reflect.model.ReflectionModuleManager;
import org.eclipse.ceylon.model.loader.model.LazyModule;
import org.eclipse.ceylon.model.loader.model.LazyPackage;
import org.eclipse.ceylon.model.runtime.CeylonModuleClassLoader;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.ModuleImport;
import org.eclipse.ceylon.model.typechecker.model.Modules;
import org.eclipse.ceylon.model.typechecker.model.Package;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.Unit;

import org.eclipse.ceylon.compiler.java.runtime.model.RuntimeModelLoader;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

public class RuntimeModuleManager extends ReflectionModuleManager implements StaticMetamodelLoader {

    private RuntimeResolver runtimeResolver;
    private boolean manualMetamodelSetup;

    public RuntimeModuleManager(RuntimeResolver runtimeResolver) {
        super();
        this.runtimeResolver = runtimeResolver;
    }

    @Override
    protected void loadPackageDescriptors() {
        // DO NOT load package descriptors here because that happens before any
        // manual setup, and so will trigger lazy-loading of modules before we've
        // had a chance to set them up: this is done after we've manually set up the
        // language module in loadModule instead
    }
    
    @Override
    public boolean isModuleLoadedFromSource(String moduleName) {
        return false;
    }
    
    @Override
    protected AbstractModelLoader createModelLoader(Modules modules) {
        return new RuntimeModelLoader(this, modules);
    }

    @Override
    public Package createPackage(String pkgName, Module module) {
        final Package pkg = new LazyPackage(getModelLoader());
        List<String> name = pkgName.isEmpty() ? 
                Collections.<String>emptyList() : 
                    splitModuleName(pkgName); 
        pkg.setName(name);
        if (module != null) {
            module.getPackages().add(pkg);
            pkg.setModule(module);
        }
        return pkg;
    }

    @Override
    protected Module createModule(List<String> moduleName, String version) {
        Module module;
        module = new ReflectionModule(this);
        module.setName(moduleName);
        module.setVersion(version);
        if(module instanceof ReflectionModule)
            setupIfJDKModule((LazyModule) module);
        return module;
    }

    public boolean loadModule(String name, String version, 
            ArtifactResult artifact, ClassLoader classLoader) {
        return loadModule(name, version, artifact, classLoader, false);
    }
    
    public boolean loadModule(String name, String version, 
            ArtifactResult artifact, ClassLoader classLoader, boolean staticMetamodel) {
        RuntimeModelLoader modelLoader = getModelLoader();
        synchronized(modelLoader.getLock()){
            manualMetamodelSetup = true;
            Module module = getOrCreateModule(splitModuleName(name), version);
            // The default module is created as available, so we use a different test for it, because we are the only
            // ones setting the module's Unit
            if(module.isDefaultModule() 
                    ? module.getUnit() != null
                    : module.isAvailable())
                return false;
            modelLoader.addModuleToClassPath(module, artifact);
            modelLoader.addModuleClassLoader(module, classLoader);
            module.setAvailable(true);
            Unit u = new Unit();
            u.setFilename(artifact.name());
            if(artifact.artifact() != null) {
                u.setFullPath(artifact.artifact().getAbsolutePath());
            }
            module.setUnit(u);
            
            if(module.isLanguageModule())
                modelLoader.loadPackageDescriptors();

            if(!module.isDefaultModule()){
                // FIXME: dependencies of Ceylon modules?
                if(!modelLoader.loadCompiledModule(module, !staticMetamodel)){
                    // we didn't find module.class so it must be a java module if it's not the default module
                    ((LazyModule)module).setJava(true);
                    module.setNativeBackends(Backend.Java.asSet());

                    // Java modules must have their dependencies set by the artifact result, as there is no module info in the jar
                    loadModuleImportsFromArtifact(module, artifact);
                }else if(staticMetamodel){
                    // for a static metamodel we get the dependencies from the artifact too
                    loadModuleImportsFromArtifact(module, artifact);
                }
            }
            return true;
        }
    }
    
    private void loadModuleImportsFromArtifact(Module module, ArtifactResult artifact) {
        for (ArtifactResult dep : artifact.dependencies()) {
            Module dependency = 
                    getOrCreateModule(splitModuleName(dep.name()), 
                            dep.version());

            ModuleImport depImport = 
                    findImport(module, dependency);
            // FIXME: apply overrides?
            // FIXME: exclude optionals and TEST deps?
            if (depImport == null) {
                ModuleImport moduleImport = 
                        new ModuleImport(dep.namespace(), dependency, dep.optional(), 
                            dep.exported(), Backend.Java);
                module.addImport(moduleImport);
            }
        }
    }

    @Override
    public Module getOrCreateModule(List<String> moduleName, String version) {
        // Override to support getting the runtime version of the Module
        version = runtimeVersion(ModelUtil.formatPath(moduleName), version);
        return super.getOrCreateModule(moduleName, version);
    }

    protected String runtimeVersion(String moduleName, String version) {
        RuntimeResolver runtimeResolver = this.runtimeResolver;
        if (runtimeResolver == null){
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if(contextClassLoader instanceof CeylonModuleClassLoader) {
                runtimeResolver = ((CeylonModuleClassLoader) contextClassLoader).getRuntimeResolver();
            }
        }
        if (runtimeResolver != null) {
            version = runtimeResolver.resolveVersion(moduleName, version);
        }
        return version;
    }
    
    public Module findLoadedModule(String moduleName, String searchedVersion) {
        return super.findLoadedModule(moduleName, runtimeVersion(moduleName, searchedVersion));
    }
    
    @Override
    public RuntimeModelLoader getModelLoader() {
        return (RuntimeModelLoader) super.getModelLoader();
    }
    
    public Module findModuleForClass(java.lang.Class<?> klass){
        return getModelLoader().findModuleForClass(klass);
    }
    
    @SuppressWarnings("serial")
    private final LinkedHashMap<TypeDescriptor, Type> typeCache = 
            new LinkedHashMap<TypeDescriptor, Type>(100, (float)0.75, true) {
        @Override
        protected boolean removeEldestEntry
                (Map.Entry<TypeDescriptor,Type> eldest) {
            return size() > 100;
        }
    };
    
    public Type getCachedType(TypeDescriptor td) {
        Type pt = typeCache.get(td);
        if (pt == null) {
            pt = td.toType(this);
            typeCache.put(td, pt);
        }
        return pt;
    }
    
    @SuppressWarnings("serial")
    private final LinkedHashMap<String, Boolean> isCache = 
            new LinkedHashMap<String, Boolean>(100, (float)0.75, true) {
        @Override
        protected boolean removeEldestEntry
                (Map.Entry<String,Boolean> eldest) {
            return size() > 100;
        }
    };
    
    public boolean cachedIs(Object o, TypeDescriptor type) {
        TypeDescriptor instanceType = Metamodel.getTypeDescriptor(o);
        String key = instanceType+"<:"+type;
        Boolean cachedResult = isCache.get(key);
        if (cachedResult != null) {
            return cachedResult;
        }
        
        if(instanceType == null)
            return false;
        Type pt1 = getCachedType(instanceType);
        Type pt2  = getCachedType(type);
        boolean result = pt1.isSubtypeOf(pt2);
        isCache.put(key, result);
        return result;
    }
    
    @Override
    protected void loadStaticMetamodel() {
        InputStream is = JvmBackendUtil.getStaticMetamodelInputStream(getClass());
        if(is != null){
            List<String> dexEntries = AndroidUtil.isRunningAndroid() ? AndroidUtil.getDexEntries() : JvmBackendUtil.getCurrentJarEntries();
            JvmBackendUtil.loadStaticMetamodel(is, dexEntries, this);
        }
    }

    @Override
    public void loadModule(String name, String version, ArtifactResult artifact) {
        loadModule(name, version, artifact, getClass().getClassLoader(), true);
    }
    
    @Override
    public boolean isManualMetamodelSetup() {
        return manualMetamodelSetup;
    }
}

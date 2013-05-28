package com.redhat.ceylon.compiler.java.runtime.model;

import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.loader.impl.reflect.model.ReflectionModule;
import com.redhat.ceylon.compiler.loader.impl.reflect.model.ReflectionModuleManager;
import com.redhat.ceylon.compiler.loader.model.LazyModule;
import com.redhat.ceylon.compiler.loader.model.LazyPackage;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Modules;
import com.redhat.ceylon.compiler.typechecker.model.Package;

public class RuntimeModuleManager extends ReflectionModuleManager {

    public RuntimeModuleManager(Context context) {
        super(context);
    }

    @Override
    protected boolean isModuleLoadedFromSource(String moduleName) {
        return false;
    }
    
    @Override
    protected AbstractModelLoader createModelLoader(Modules modules) {
        return new RuntimeModelLoader(this, modules);
    }

    @Override
    protected Package createPackage(String pkgName, Module module) {
        final Package pkg = new LazyPackage(getModelLoader());
        List<String> name = pkgName.isEmpty() ? Collections.<String>emptyList() : splitModuleName(pkgName); 
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

    public void loadModule(String name, String version, ArtifactResult artifact, ClassLoader classLoader) {
        Module module = getOrCreateModule(splitModuleName(name), version);
        RuntimeModelLoader modelLoader = getModelLoader();
        modelLoader.addModuleToClassPath(module, artifact);
        modelLoader.addModuleClassLoader(module, classLoader);
        module.setAvailable(true);
        
        if(!module.isDefault()){
            // FIXME: dependencies of Ceylon modules?
            if(!modelLoader.loadCompiledModule(module)){
                // we didn't find module.class so it must be a java module if it's not the default module
                ((LazyModule)module).setJava(true);

                // Java modules must have their dependencies set by the artifact result, as there is no module info in the jar
                for (ArtifactResult dep : artifact.dependencies()) {
                    Module dependency = getOrCreateModule(ModuleManager.splitModuleName(dep.name()), dep.version());

                    ModuleImport depImport = findImport(module, dependency);
                    if (depImport == null) {
                        ModuleImport moduleImport = new ModuleImport(dependency, false, false);
                        module.getImports().add(moduleImport);
                    }
                }
            }
        }
    }
    
    @Override
    public RuntimeModelLoader getModelLoader() {
        return (RuntimeModelLoader) super.getModelLoader();
    }
    
    public Module findModuleForClass(java.lang.Class<?> klass){
        return getModelLoader().findModuleForClass(klass);
    }
}

package com.redhat.ceylon.compiler.reflectionmodelloader;

import java.util.List;

import com.redhat.ceylon.compiler.modelloader.AbstractModelLoader;
import com.redhat.ceylon.compiler.modelloader.model.LazyModuleManager;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Modules;

public class ReflectionModuleManager extends LazyModuleManager {

    private AbstractModelLoader modelLoader;

    @Override
    public void initCoreModules() {
        super.initCoreModules();
        Modules modules = getContext().getModules();
        // FIXME: this should go away somewhere else, but we need it to be set otherwise
        // when we load the module from compiled sources, ModuleManager.getOrCreateModule() will not
        // return the language module because its version is null
        Module languageModule = modules.getLanguageModule();
        languageModule.setVersion(TypeChecker.LANGUAGE_MODULE_VERSION);
    }
    
    @Override
    public AbstractModelLoader getModelLoader() {
        if(modelLoader == null){
            Modules modules = getContext().getModules();
            modelLoader = new ReflectionModelLoader(this, modules);            
        }
        return modelLoader;
    }

    @Override
    protected Module createModule(List<String> moduleName) {
        Module module = new ReflectionModule(this);
        module.setName(moduleName);
        return module;
    }

    @Override
    public void resolveModule(Module module, VirtualFile artifact, List<PhasedUnits> phasedUnitsOfDependencies) {
        // FIXME: implement
        System.err.println("Resolve module: "+module);
        super.resolveModule(module, artifact, phasedUnitsOfDependencies);
    }

    @Override
    public void prepareForTypeChecking() {
        getModelLoader().loadStandardModules();
        getModelLoader().loadPackageDescriptors();
    }
}

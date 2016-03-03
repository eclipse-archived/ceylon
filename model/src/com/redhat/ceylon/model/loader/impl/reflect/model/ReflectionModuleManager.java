package com.redhat.ceylon.model.loader.impl.reflect.model;

import java.util.List;

import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.loader.JvmBackendUtil;
import com.redhat.ceylon.model.loader.model.LazyModule;
import com.redhat.ceylon.model.loader.model.LazyModuleManager;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Modules;

public abstract class ReflectionModuleManager extends LazyModuleManager {

    private AbstractModelLoader modelLoader;

    public ReflectionModuleManager() {
        super();
    }

    @Override
    public void initCoreModules(Modules modules) {
        super.initCoreModules(modules);
        // FIXME: this should go away somewhere else, but we need it to be set otherwise
        // when we load the module from compiled sources, ModuleManager.getOrCreateModule() will not
        // return the language module because its version is null
        Module languageModule = modules.getLanguageModule();
        languageModule.setVersion(Versions.CEYLON_VERSION_NUMBER);
    }
    
    @Override
    public AbstractModelLoader getModelLoader() {
        if(modelLoader == null){
            modelLoader = createModelLoader(modules);            
        }
        return modelLoader;
    }

    protected abstract AbstractModelLoader createModelLoader(Modules modules);

    @Override
    protected Module createModule(List<String> moduleName, String version) {
        Module module;
        if(isModuleLoadedFromSource(JvmBackendUtil.getName(moduleName)))
            module = new Module();
        else
            module = new ReflectionModule(this);
        module.setName(moduleName);
        module.setVersion(version);
        if(module instanceof ReflectionModule)
            setupIfJDKModule((LazyModule) module);
        return module;
    }

    @Override
    public void prepareForTypeChecking() {
        // make sure we don't load ceylon.language from its class files if we're documenting it
        if(!isModuleLoadedFromSource(AbstractModelLoader.CEYLON_LANGUAGE)){
            loadStaticMetamodel();
            getModelLoader().loadStandardModules();
        }
        getModelLoader().loadPackageDescriptors();
    }
    
    protected void loadStaticMetamodel() {}

    @Override
    public void modulesVisited() {
        // if we're documenting ceylon.language, we didn't call loadStandardModules() so we need
        // to call that.
        if(isModuleLoadedFromSource(AbstractModelLoader.CEYLON_LANGUAGE)){
            getModelLoader().setupWithNoStandardModules();
        }
    }
    
    @Override
    public boolean shouldLoadTransitiveDependencies() {
        return true;
    }
}

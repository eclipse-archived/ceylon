package com.redhat.ceylon.ceylondoc;

import java.util.List;

import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.loader.impl.reflect.ReflectionModelLoader;
import com.redhat.ceylon.compiler.loader.impl.reflect.model.ReflectionModuleManager;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.Modules;

public class CeylonDocModuleManager extends ReflectionModuleManager {

    private List<ModuleSpec> modulesSpecs;
    private Logger log;

    public CeylonDocModuleManager(Context context, List<ModuleSpec> modules, Logger log) {
        super(context);
        this.modulesSpecs = modules;
        this.log = log;
    }

    @Override
    protected boolean isModuleLoadedFromSource(String moduleName) {
        for(ModuleSpec spec : modulesSpecs){
            if(spec.name.equals(moduleName))
                return true;
        }
        return false;
    }
    
    @Override
    protected AbstractModelLoader createModelLoader(Modules modules) {
        return new ReflectionModelLoader(this, modules){
            @Override
            protected void logError(String message) {
                log.error(message);
            }
            @Override
            protected void logVerbose(String message) {
                log.debug(message);
            }
            @Override
            protected void logWarning(String message) {
                log.warning(message);
            }
        };
    }
}

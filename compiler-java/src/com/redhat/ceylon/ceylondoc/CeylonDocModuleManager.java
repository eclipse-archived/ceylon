package com.redhat.ceylon.ceylondoc;

import java.util.List;

import com.redhat.ceylon.compiler.loader.impl.reflect.model.ReflectionModuleManager;
import com.redhat.ceylon.compiler.typechecker.context.Context;

public class CeylonDocModuleManager extends ReflectionModuleManager {

    private List<ModuleSpec> modulesSpecs;

    public CeylonDocModuleManager(Context context, List<ModuleSpec> modules) {
        super(context);
        this.modulesSpecs = modules;
    }

    @Override
    protected boolean isModuleLoadedFromSource(String moduleName) {
        for(ModuleSpec spec : modulesSpecs){
            if(spec.name.equals(moduleName))
                return true;
        }
        return false;
    }
}

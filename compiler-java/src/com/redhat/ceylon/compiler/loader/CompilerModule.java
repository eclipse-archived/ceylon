package com.redhat.ceylon.compiler.loader;

import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.sun.tools.javac.util.Context;

public class CompilerModule extends Module {

    private Context context;
    private CeylonModelLoader modelLoader;

    public CompilerModule(com.sun.tools.javac.util.Context context) {
        this.context = context;
    }

    public CompilerModule(CeylonModelLoader modelLoader) {
        this.modelLoader = modelLoader;
    }

    @Override
    public Package getPackage(String name) {
        System.err.println("Looking up package "+name);
        Package pkg = super.getPackage(name);
        if(pkg == null)
            pkg = getModelLoader().findOrCreatePackage(this, name);
        return pkg;
    }

    private CeylonModelLoader getModelLoader() {
        if(modelLoader == null){
            modelLoader = CeylonModelLoader.instance(context);
        }
        return modelLoader;
    }

}

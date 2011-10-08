package com.redhat.ceylon.compiler.loader;

import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.sun.tools.javac.util.Context;

public class CompilerModule extends Module {

    private Context context;

    public CompilerModule(com.sun.tools.javac.util.Context context) {
        this.context = context;
    }
    
    @Override
    public Package getPackage(String name) {
        System.err.println("Looking up package "+name);
        Package pkg = super.getPackage(name);
        if(pkg == null)
            pkg = CeylonModelLoader.instance(context).findOrCreatePackage(this, name);
        return pkg;
    }

}

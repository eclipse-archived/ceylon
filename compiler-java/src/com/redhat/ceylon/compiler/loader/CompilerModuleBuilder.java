package com.redhat.ceylon.compiler.loader;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleBuilder;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.Module;

public class CompilerModuleBuilder extends ModuleBuilder {

    private com.sun.tools.javac.util.Context context;

    public CompilerModuleBuilder(Context ceylonContext, com.sun.tools.javac.util.Context context) {
        super(ceylonContext);
        this.context = context;
    }

    @Override
    protected Module createModule(List<String> moduleName) {
        Module module = new CompilerModule(context);
        module.setName(moduleName);
        return module;
    }
}

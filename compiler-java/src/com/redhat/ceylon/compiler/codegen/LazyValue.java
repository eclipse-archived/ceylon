package com.redhat.ceylon.compiler.codegen;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.code.Symbol.ClassSymbol;

public class LazyValue extends Value {
    public ClassSymbol classSymbol;
    private ModelCompleter completer;
    private boolean isLoaded = false;

    public LazyValue(ClassSymbol classSymbol, ModelCompleter completer) {
        this.classSymbol = classSymbol;
        this.completer = completer;
        setName(Util.strip(classSymbol.getSimpleName().toString()));
    }

    private void load() {
        if(!isLoaded){
            isLoaded = true;
            completer.complete(this);
        }
    }

    @Override
    public boolean isVariable() {
        load();
        return super.isVariable();
    }
    
    @Override
    public ProducedType getType() {
        load();
        return super.getType();
    }

}

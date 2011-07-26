package com.redhat.ceylon.compiler.loader;

import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.code.Symbol.ClassSymbol;

public class LazyMethod extends Method {
    public ClassSymbol classSymbol;
    private ModelCompleter completer;
    private boolean isLoaded = false;

    public LazyMethod(ClassSymbol classSymbol, ModelCompleter completer) {
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
    public ProducedType getType() {
        load();
        return super.getType();
    }

}

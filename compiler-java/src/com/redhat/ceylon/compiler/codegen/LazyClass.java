package com.redhat.ceylon.compiler.codegen;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.sun.tools.javac.code.Symbol.ClassSymbol;

public class LazyClass extends Class {

    public ClassSymbol classSymbol;
    private ModelCompleter completer;
    private boolean isLoaded = false;

    public LazyClass(ClassSymbol classSymbol, ModelCompleter completer) {
        this.classSymbol = classSymbol;
        this.completer = completer;
    }

    @Override
    public List<Declaration> getMembers() {
        load();
        return super.getMembers();
    }
    
    private void load() {
        if(!isLoaded){
            isLoaded = true;
            completer.complete(this);
        }
    }

    @Override
    public ProducedType getExtendedType() {
        load();
        return super.getExtendedType();
    }
    
    @Override
    public List<TypeParameter> getTypeParameters() {
        load();
        return super.getTypeParameters();
    }
}

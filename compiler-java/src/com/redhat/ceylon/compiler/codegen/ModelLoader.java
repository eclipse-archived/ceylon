package com.redhat.ceylon.compiler.codegen;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;

public interface ModelLoader {
    public ProducedType getType(String name, Scope scope);
}

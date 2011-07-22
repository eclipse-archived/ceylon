package com.redhat.ceylon.compiler.codegen;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;

public interface ModelLoader {
    public Declaration getDeclaration(String typeName);
    public ProducedType getType(String name, Scope scope);
}

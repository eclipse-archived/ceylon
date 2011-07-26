package com.redhat.ceylon.compiler.loader;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;

public interface ModelLoader {
    
    enum DeclarationType {
        TYPE, VALUE;
    }
    
    public Declaration getDeclaration(String typeName, DeclarationType declarationType);
    public ProducedType getType(String name, Scope scope);
}

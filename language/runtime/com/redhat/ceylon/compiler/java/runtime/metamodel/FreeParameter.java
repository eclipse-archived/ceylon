package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.untyped.Parameter;
import ceylon.language.metamodel.untyped.Parameter$impl;
import ceylon.language.metamodel.untyped.Type;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeParameter implements Parameter, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeParameter.class);
    private com.redhat.ceylon.compiler.typechecker.model.Parameter declaration;
    private Type type;
    
    FreeParameter(com.redhat.ceylon.compiler.typechecker.model.Parameter declaration){
        this.declaration = declaration;
        this.type = Metamodel.getMetamodel(declaration.getType());
    }
    
    @Override
    @Ignore
    public Parameter$impl $ceylon$language$metamodel$untyped$Parameter$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::String")
    public String getName() {
        return declaration.getName();
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.untyped::Type")
    public Type getType() {
        return type;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }

}

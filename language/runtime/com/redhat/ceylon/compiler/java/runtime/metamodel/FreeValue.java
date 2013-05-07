package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.untyped.Type;
import ceylon.language.metamodel.untyped.Value$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeValue 
    extends FreeDeclaration
    implements ceylon.language.metamodel.untyped.Value {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeValue.class);
    
    private Type type;

    public FreeValue(com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration declaration) {
        super(declaration);

        this.type = Metamodel.getMetamodel(declaration.getType());
    }

    @Override
    @Ignore
    public Value$impl $ceylon$language$metamodel$untyped$Value$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Value<ceylon.language::Anything>")
    public ceylon.language.metamodel.Value<? extends Object> getApplied() {
        com.redhat.ceylon.compiler.typechecker.model.Value modelDecl = (com.redhat.ceylon.compiler.typechecker.model.Value)declaration;
        return modelDecl.isVariable() ? new AppliedVariable(this, modelDecl.getType(), null) : new AppliedValue(this, modelDecl.getType(), null);
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

package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.Attribute$impl;
import ceylon.language.metamodel.AttributeType$impl;
import ceylon.language.metamodel.DeclarationType$impl;
import ceylon.language.metamodel.Value;
import ceylon.language.metamodel.declaration.AttributeDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

public class AppliedAttribute<Container, Type> 
    extends AppliedMember<Container, ceylon.language.metamodel.Value<? extends Type>>
    implements ceylon.language.metamodel.Attribute<Container, Type> {

    private FreeAttribute declaration;
    private ProducedType type;
    private ceylon.language.metamodel.Type closedType;

    public AppliedAttribute(TypeDescriptor $reifiedType, TypeDescriptor $reifiedKind,
                            FreeAttribute declaration, ProducedType type) {
        super($reifiedType, $reifiedKind);
        this.declaration = declaration;
        this.type = type;
        this.closedType = Metamodel.getAppliedMetamodel(type);
    }

    @Override
    @Ignore
    public AttributeType$impl<Type> $ceylon$language$metamodel$AttributeType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public DeclarationType$impl $ceylon$language$metamodel$DeclarationType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Attribute$impl<Container, Type> $ceylon$language$metamodel$Attribute$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.declaration::AttributeDeclaration")
    public AttributeDeclaration getDeclaration() {
        return declaration;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Type")
    public ceylon.language.metamodel.Type getType() {
        return closedType;
    }
    
    @Override
    protected Value<? extends Type> bindTo(Object instance) {
        // FIXME: add AttributeDeclaration.isVariable or something
        return (((com.redhat.ceylon.compiler.typechecker.model.Value)declaration.declaration).isVariable() 
                ? new AppliedVariable(null, declaration, type, instance) 
                : new AppliedValue(null, declaration, type, instance));
    }
}

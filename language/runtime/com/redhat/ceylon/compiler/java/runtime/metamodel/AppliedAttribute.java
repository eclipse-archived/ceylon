package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.Attribute$impl;
import ceylon.language.metamodel.AttributeModel$impl;
import ceylon.language.metamodel.Model$impl;
import ceylon.language.metamodel.Value;
import ceylon.language.metamodel.declaration.AttributeDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Generic;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Container", variance = Variance.IN),
    @TypeParameter(value = "Type", variance = Variance.OUT),
})
public class AppliedAttribute<Container, Type> 
    extends AppliedMember<Container, ceylon.language.metamodel.Value<? extends Type>>
    implements ceylon.language.metamodel.Attribute<Container, Type> {

    protected FreeAttribute declaration;
    protected ProducedType type;
    private ceylon.language.metamodel.Type closedType;
    @Ignore
    protected final TypeDescriptor $reifiedType;
    
    @Override
    public String toString() {
        return declaration.toString();
    }
    
    public AppliedAttribute(@Ignore TypeDescriptor $reifiedContainer, 
                            @Ignore TypeDescriptor $reifiedType,
                            FreeAttribute declaration, ProducedType type,
                            ceylon.language.metamodel.ClassOrInterface<? extends Object> container) {
        super($reifiedContainer, TypeDescriptor.klass(ceylon.language.metamodel.Value.class, $reifiedType), container);
        this.declaration = declaration;
        this.type = type;
        this.closedType = Metamodel.getAppliedMetamodel(type);
        this.$reifiedType = $reifiedType;
    }

    @Override
    @Ignore
    public AttributeModel$impl<Type> $ceylon$language$metamodel$AttributeModel$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Model$impl $ceylon$language$metamodel$Model$impl() {
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
        return new AppliedValue($reifiedType, declaration, type, instance);
    }
    
    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedAttribute.class, super.$reifiedType, $reifiedType);
    }

    public static ceylon.language.metamodel.Attribute instance(@Ignore TypeDescriptor $reifiedSubType, @Ignore TypeDescriptor reifiedValueType, 
                                                               FreeAttribute value, ProducedType valueType, 
                                                               com.redhat.ceylon.compiler.typechecker.model.Value decl,
                                                               ceylon.language.metamodel.ClassOrInterface<? extends Object> container) {
        // if the container has no TP, the declaration will also be an Attribute
        if(!Metamodel.hasTypeParameters((Generic) decl.getContainer()))
            return (ceylon.language.metamodel.Attribute) Metamodel.getOrCreateMetamodel(decl);
        return decl.isVariable()
                ? new AppliedVariableAttribute($reifiedSubType, reifiedValueType, value, valueType, container)
                : new AppliedAttribute($reifiedSubType, reifiedValueType, value, valueType, container);
    }
}

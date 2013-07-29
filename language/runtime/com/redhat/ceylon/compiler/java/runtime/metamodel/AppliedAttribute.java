package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.model.Attribute$impl;
import ceylon.language.model.AttributeModel$impl;
import ceylon.language.model.Model$impl;
import ceylon.language.model.Value;
import ceylon.language.model.declaration.ValueDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Generic;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Container", variance = Variance.IN),
    @TypeParameter(value = "Type", variance = Variance.OUT),
})
public class AppliedAttribute<Container, Type> 
    extends AppliedMember<Container, ceylon.language.model.Value<? extends Type>>
    implements ceylon.language.model.Attribute<Container, Type> {

    protected FreeAttribute declaration;
    protected ProducedTypedReference typedReference;
    private ceylon.language.model.Type closedType;
    @Ignore
    protected final TypeDescriptor $reifiedType;
    
    @Override
    public String toString() {
        return Metamodel.getProducedTypedReferenceString(typedReference);
    }
    
    public AppliedAttribute(@Ignore TypeDescriptor $reifiedContainer, 
                            @Ignore TypeDescriptor $reifiedType,
                            FreeAttribute declaration, ProducedTypedReference typedReference,
                            ceylon.language.model.ClassOrInterface<? extends Object> container) {
        super($reifiedContainer, TypeDescriptor.klass(ceylon.language.model.Value.class, $reifiedType), container);
        this.declaration = declaration;
        this.typedReference = typedReference;
        this.closedType = Metamodel.getAppliedMetamodel(typedReference.getType());
        this.$reifiedType = $reifiedType;
    }

    @Override
    @Ignore
    public AttributeModel$impl<Type> $ceylon$language$model$AttributeModel$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Model$impl $ceylon$language$model$Model$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Attribute$impl<Container, Type> $ceylon$language$model$Attribute$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.model.declaration::ValueDeclaration")
    public ValueDeclaration getDeclaration() {
        return declaration;
    }

    @Override
    @TypeInfo("ceylon.language.model::Type")
    public ceylon.language.model.Type getType() {
        return closedType;
    }
    
    @Override
    protected Value<? extends Type> bindTo(Object instance) {
        return new AppliedValue($reifiedType, declaration, typedReference, instance);
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedAttribute.class, super.$reifiedType, $reifiedType);
    }

    public static ceylon.language.model.Attribute instance(@Ignore TypeDescriptor $reifiedSubType, @Ignore TypeDescriptor reifiedValueType, 
                                                               FreeAttribute value, ProducedTypedReference valueTypedReference, 
                                                               com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration decl,
                                                               ceylon.language.model.ClassOrInterface<? extends Object> container) {
        // if the container has no TP, the declaration will also be an Attribute
        if(!Metamodel.hasTypeParameters((Generic) decl.getContainer()))
            return (ceylon.language.model.Attribute) Metamodel.getOrCreateMetamodel(decl);
        return decl.isVariable()
                ? new AppliedVariableAttribute($reifiedSubType, reifiedValueType, value, valueTypedReference, container)
                : new AppliedAttribute($reifiedSubType, reifiedValueType, value, valueTypedReference, container);
    }
}

package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.model.ClassOrInterface;
import ceylon.language.model.Interface;
import ceylon.language.model.InterfaceModel$impl;
import ceylon.language.model.Member$impl;
import ceylon.language.model.MemberInterface$impl;
import ceylon.language.model.declaration.InterfaceDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

public class AppliedMemberInterface<Container, Type> 
    extends AppliedClassOrInterface<Type>
    implements ceylon.language.model.MemberInterface<Container, Type> {

    @Ignore
    private TypeDescriptor $reifiedContainer;

    AppliedMemberInterface(@Ignore TypeDescriptor $reifiedContainer,
                           @Ignore TypeDescriptor $reifiedType,
                           ProducedType producedType) {
        super($reifiedType, producedType);
        this.$reifiedContainer = $reifiedContainer;
    }

    @Override
    @Ignore
    public InterfaceModel$impl<Type> $ceylon$language$model$InterfaceModel$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Member$impl<Container, Interface<? extends Type>> $ceylon$language$model$Member$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public MemberInterface$impl<Container, Type> $ceylon$language$model$MemberInterface$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Interface<? extends Type> $call() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Interface<? extends Type> $call(Object arg0) {
        return new AppliedInterface($reifiedType, super.producedType);
    }

    @Override
    @Ignore
    public Interface<? extends Type> $call(Object arg0, Object arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Interface<? extends Type> $call(Object arg0, Object arg1, Object arg2) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Interface<? extends Type> $call(Object... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public short $getVariadicParameterIndex() {
        return -1;
    }

    @Override
    @TypeInfo("ceylon.language.model.declaration::InterfaceDeclaration")
    public InterfaceDeclaration getDeclaration() {
        return (InterfaceDeclaration) super.getDeclaration();
    }
    
    @Override
    @TypeInfo("ceylon.language.model::ClassOrInterface<ceylon.language::Anything>")
    public ceylon.language.model.ClassOrInterface<? extends Object> getDeclaringClassOrInterface() {
        return (ClassOrInterface<? extends Object>) Metamodel.getAppliedMetamodel(producedType.getQualifyingType());
    }

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedMemberInterface.class, $reifiedContainer, $reifiedType);
    }
}

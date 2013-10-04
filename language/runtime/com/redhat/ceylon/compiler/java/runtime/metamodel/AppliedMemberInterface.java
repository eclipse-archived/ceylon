package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.model.ClassOrInterface;
import ceylon.language.meta.model.Interface;
import ceylon.language.meta.model.InterfaceModel$impl;
import ceylon.language.meta.model.Member$impl;
import ceylon.language.meta.model.MemberInterface$impl;
import ceylon.language.meta.declaration.InterfaceDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

public class AppliedMemberInterface<Container, Type> 
    extends AppliedClassOrInterface<Type>
    implements ceylon.language.meta.model.MemberInterface<Container, Type> {

    @Ignore 
    final TypeDescriptor $reifiedContainer;

    AppliedMemberInterface(@Ignore TypeDescriptor $reifiedContainer,
                           @Ignore TypeDescriptor $reifiedType,
                           ProducedType producedType) {
        super($reifiedType, producedType);
        this.$reifiedContainer = $reifiedContainer;
    }

    @Override
    @Ignore
    public InterfaceModel$impl<Type> $ceylon$language$meta$model$InterfaceModel$impl() {
        return null;
    }

    @Override
    @Ignore
    public Member$impl<Container, Interface<? extends Type>> $ceylon$language$meta$model$Member$impl() {
        return null;
    }

    @Override
    @Ignore
    public MemberInterface$impl<Container, Type> $ceylon$language$meta$model$MemberInterface$impl() {
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
        return new AppliedInterface<Type>($reifiedType, super.producedType, getContainer(), arg0);
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
    @TypeInfo("ceylon.language.meta.declaration::InterfaceDeclaration")
    public InterfaceDeclaration getDeclaration() {
        return (InterfaceDeclaration) super.getDeclaration();
    }
    
    @Override
    @TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>")
    public ceylon.language.meta.model.Type<? extends Object> getDeclaringType() {
        return Metamodel.getAppliedMetamodel(producedType.getQualifyingType());
    }

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedMemberInterface.class, $reifiedContainer, $reifiedType);
    }

    @Override
    @Ignore
    public Interface<? extends Type> $call$variadic() {
        return $call$variadic(empty_.get_());
    }
    
    @Override
    @Ignore
    public Interface<? extends Type> $call$variadic(
            Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Interface<? extends Type> $call$variadic(
            Object arg0, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Interface<? extends Type> $call$variadic(
            Object arg0, Object arg1, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Interface<? extends Type> $call$variadic(
            Object arg0, Object arg1, Object arg2, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Interface<? extends Type> $call$variadic(Object... argsAndVarargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Interface<? extends Type> $call$variadic(Object arg0) {
        return $call$variadic(arg0, empty_.get_());
    }

    @Override
    @Ignore
    public Interface<? extends Type> $call$variadic(Object arg0, Object arg1) {
        return $call$variadic(arg0, arg1, empty_.get_());
    }

    @Override
    @Ignore
    public Interface<? extends Type> $call$variadic(Object arg0, Object arg1,
            Object arg2) {
        return $call$variadic(arg0, arg1, arg2, empty_.get_());
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 37 * result + getDeclaringType().hashCode();
        result = 37 * result + getDeclaration().hashCode();
        result = 37 * result + getTypeArguments().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.meta.model.MemberInterface == false)
            return false;
        ceylon.language.meta.model.MemberInterface<?, ?> other = (ceylon.language.meta.model.MemberInterface<?, ?>) obj;
        return getDeclaration().equals(other.getDeclaration())
                && getDeclaringType().equals(other.getDeclaringType())
                && getTypeArguments().equals(other.getTypeArguments());
    }

    @Override
    @TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>")
    public ceylon.language.meta.model.Type<? extends java.lang.Object> getContainer(){
        return getDeclaringType();
    }
}

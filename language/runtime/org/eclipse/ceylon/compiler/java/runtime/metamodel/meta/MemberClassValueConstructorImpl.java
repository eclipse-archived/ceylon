/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.metamodel.meta;

import org.eclipse.ceylon.common.NonNull;
import org.eclipse.ceylon.compiler.java.Util;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.SatisfiedTypes;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.metadata.Variance;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ValueConstructorDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;
import org.eclipse.ceylon.model.typechecker.model.TypedReference;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.ClassImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.MemberClassImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.MemberClassValueConstructorImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.MemberImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.ValueConstructorImpl;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.ValueConstructorDeclaration;
import ceylon.language.meta.model.Member;
import ceylon.language.meta.model.MemberClass;
import ceylon.language.meta.model.MemberClassValueConstructor;
import ceylon.language.meta.model.ValueConstructor;


@Ceylon(major=8)
@org.eclipse.ceylon.compiler.java.metadata.Class
@SatisfiedTypes("ceylon.language.meta.model::MemberClassValueConstructor<Container,Get>")
@TypeParameters({
    @TypeParameter(value = "Container", variance = Variance.IN),
    @TypeParameter(value = "Get", variance = Variance.OUT)
})
public class MemberClassValueConstructorImpl<Container, Get>
        extends MemberImpl<Container, ceylon.language.meta.model.ValueConstructor<? extends Get>> 
        implements MemberClassValueConstructor<Container, Get> {

    public final MemberClassImpl<Container, Get, ?> clazz;
    
    protected final ValueConstructorDeclarationImpl declaration;
    protected final TypedReference typedReference;
//    private final ceylon.language.meta.model.Type<? extends Get> closedType;
    @Ignore
    protected final TypeDescriptor $reifiedGet;
    
    @Ignore
    public MemberClassValueConstructorImpl(TypeDescriptor $reifiedContainer,
            TypeDescriptor $reifiedGet,
            ValueConstructorDeclarationImpl declaration, TypedReference typedReference,
            MemberClassImpl<Container, Get, ?> clazz) {
        super($reifiedContainer, TypeDescriptor.klass(ceylon.language.meta.model.Value.class, $reifiedGet), clazz.getContainer());
        this.declaration = declaration;
        this.typedReference = typedReference;
//        this.closedType = Metamodel.getAppliedMetamodel(typedReference.getType());
        this.$reifiedGet = $reifiedGet;
        this.clazz = clazz;
    }
    
    @Override
    public MemberClass<Container, Get, ?> getType() {
        return clazz;
    }
    
    @Override
    public ceylon.language.meta.model.ClassModel<Get, ?> getContainer() {
        return clazz;
    }
    
    public ValueConstructorDeclaration getDeclaration() {
        return (ValueConstructorDeclaration)declaration;
    }
    
    @TypeInfo("ceylon.language.meta.model::ValueConstructor<Get>")
    @Override
    public ValueConstructor<Get> bind(Object instance) {
        return new ValueConstructorImpl(
                $reifiedGet,  
                declaration,
                typedReference,
                (ClassImpl)getContainer(),
                instance);
    }
    
    
    ////////////////////////////

    
    @Override
    protected ValueConstructor<? extends Get> bindTo(Object instance) {
        return new ValueConstructorImpl<Get>(
                $reifiedGet, declaration, typedReference, (ClassImpl)clazz.getContainer(), instance);
    }

    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(MemberClassValueConstructorImpl.class, super.$reifiedContainer, $reifiedGet);
    }

    @Override
    @Ignore
    public ValueConstructor<? extends Get> $callvariadic$() {
        return $callvariadic$(empty_.get_());
    }
    
    @Override
    @Ignore
    public ValueConstructor<? extends Get> $callvariadic$(
            Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public ValueConstructor<? extends Get> $callvariadic$(
            Object arg0, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public ValueConstructor<? extends Get> $callvariadic$(
            Object arg0, Object arg1, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public ValueConstructor<? extends Get> $callvariadic$(
            Object arg0, Object arg1, Object arg2, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public ValueConstructor<? extends Get> $callvariadic$(Object... argsAndVarargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public ValueConstructor<? extends Get> $callvariadic$(Object arg0) {
        return $callvariadic$(arg0, empty_.get_());
    }

    @Override
    @Ignore
    public ValueConstructor<? extends Get> $callvariadic$(Object arg0, Object arg1) {
        return $callvariadic$(arg0, arg1, empty_.get_());
    }

    @Override
    @Ignore
    public ValueConstructor<? extends Get> $callvariadic$(Object arg0, Object arg1,
            Object arg2) {
        return $callvariadic$(arg0, arg1, arg2, empty_.get_());
    }

    //@Override
    //public Value<? extends Get, ? super Set> bind(@TypeInfo("ceylon.language::Object") @Name("container") java.lang.Object container){
    //    return (Value<? extends Get, ? super Set>) Metamodel.bind(this, this.typedReference.getQualifyingType(), container);
    //}

    @Override
    public int hashCode() {
        int result = 1;
        result = 37 * result + getDeclaringType().hashCode();
        result = 37 * result + getDeclaration().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.meta.model.MemberClassValueConstructor == false)
            return false;
        ceylon.language.meta.model.MemberClassValueConstructor<?,?> other = (ceylon.language.meta.model.MemberClassValueConstructor<?,?>) obj;
        return getDeclaration().equals(other.getDeclaration())
                && getDeclaringType().equals(((Member<?,?>)other).getDeclaringType());
    }

    @Override
    public String toString() {
        return Metamodel.toTypeString(this);
    }
}
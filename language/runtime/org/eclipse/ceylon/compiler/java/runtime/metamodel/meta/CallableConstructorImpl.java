/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.metamodel.meta;

import ceylon.language.Array;
import ceylon.language.Map;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.CallableConstructorDeclaration;
import ceylon.language.meta.model.CallableConstructor;
import ceylon.language.meta.model.ClassModel;

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.SatisfiedTypes;
import org.eclipse.ceylon.compiler.java.metadata.Sequenced;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.metadata.Variance;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.ConstructorDispatch;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.DefaultValueProvider;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.CallableConstructorDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ClassDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.Reference;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.CallableConstructorImpl;

@Ceylon(major=8)
@org.eclipse.ceylon.compiler.java.metadata.Class
@SatisfiedTypes("ceylon.language.meta.model::CallableConstructor<Type,Arguments>")
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Arguments", variance = Variance.IN)
})
public class CallableConstructorImpl<Type, Arguments extends Sequential<? extends Object>> 
        implements CallableConstructor<Type, Arguments>, ReifiedType, DefaultValueProvider {

    private final TypeDescriptor $reified$Type;
    private final TypeDescriptor $reified$Arguments;
    
    private final ClassDeclarationImpl freeClass;
    public final ClassModel<Type,?> appliedClass;
    private final CallableConstructorDeclarationImpl freeConstructor;
    private final Reference constructorReference;
    private java.lang.Object instance;
    private volatile boolean initialised = false;
    
    private ConstructorDispatch<Type, Arguments> dispatch;
    
    @Ignore
    public CallableConstructorImpl(TypeDescriptor $reified$Type,
            TypeDescriptor $reified$Arguments, Reference appliedFunction,
            CallableConstructorDeclarationImpl freeConstructor,
            ClassModel<Type,?> clazz, Object instance) {
        this.$reified$Type = $reified$Type;
        this.$reified$Arguments = $reified$Arguments;
        this.freeClass = (ClassDeclarationImpl)clazz.getDeclaration();
        this.appliedClass = clazz;
        this.freeConstructor = freeConstructor;
        this.constructorReference = appliedFunction;
        this.instance = instance;
    }
    
    protected void checkInit(){
        if(!initialised){
            synchronized(Metamodel.getLock()){
                if(!initialised){
                    dispatch = new ConstructorDispatch<Type, Arguments>(
                            constructorReference,
                            appliedClass,
                            freeConstructor,
                            ((org.eclipse.ceylon.model.typechecker.model.Functional)freeConstructor.declaration).getFirstParameterList().getParameters(),
                            this.instance);
                    initialised = true;
                }
            }
        }
    }
    
    
    
    @Override
    public ClassModel<Type,?> getType() {
        return appliedClass;
    }
    
    @Override
    public ClassModel<Type,?> getContainer() {
        return null;
    }
    
    @Override
    public CallableConstructorDeclaration getDeclaration() {
        return (CallableConstructorDeclaration)freeConstructor;
    }
    
    @Override
    @Ignore
    public Type apply() {
        return apply(empty_.get_());
    }
    
    @Override
    public Type apply(@Name("arguments")
        @Sequenced
        @TypeInfo("ceylon.language::Sequential<ceylon.language::Anything>")
        Sequential<?> arguments){
        checkInit();
        
        dispatch.checkConstructor();
        return Metamodel.apply(this, arguments, 
                dispatch.parameterProducedTypes, 
                dispatch.firstDefaulted, 
                dispatch.variadicIndex);
    }

    @Override
    public Type namedApply(@Name("arguments")
        @TypeInfo("ceylon.language::Iterable<ceylon.language::Entry<ceylon.language::String,ceylon.language::Anything>,ceylon.language::Null>")
        ceylon.language.Iterable<? extends ceylon.language.Entry<? extends ceylon.language.String,? extends java.lang.Object>,? extends java.lang.Object> arguments){
        checkInit();
        dispatch.checkConstructor();
        return Metamodel.namedApply(this, this, 
                (org.eclipse.ceylon.model.typechecker.model.Functional)(freeConstructor != null ? freeConstructor.declaration : freeClass.declaration), 
                arguments, dispatch.getProducedParameterTypes());
    }
    
    @Override
    public Object getDefaultParameterValue(Parameter parameter, Array<Object> values, int collectedValueCount) {
        checkInit();
        return dispatch.getDefaultParameterValue(parameter, values, collectedValueCount);
    }
    
    @Override
    public Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> getTypeArgumentList() {
        return appliedClass.getTypeArgumentList();
    }
    
    @Override
    public Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.meta.model.Type<? extends Object>> getTypeArguments() {
        return appliedClass.getTypeArguments();
    }
    
    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.meta.declaration::TypeParameter,[ceylon.language.meta.model::Type<ceylon.language::Anything>,ceylon.language.meta.declaration::Variance]>")
    public Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.Sequence<? extends Object>> getTypeArgumentWithVariances() {
        return appliedClass.getTypeArgumentWithVariances();
    }
    
    @Override
    @TypeInfo("ceylon.language::Sequential<[ceylon.language.meta.model::Type<ceylon.language::Anything>,ceylon.language.meta.declaration::Variance]>")
    public ceylon.language.Sequential<? extends ceylon.language.Sequence<? extends Object>> getTypeArgumentWithVarianceList() {
        return appliedClass.getTypeArgumentWithVarianceList();
    }

    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.model::Type<ceylon.language::Anything>>")
    @Override
    public ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> getParameterTypes(){
        checkInit();
        return dispatch.getParameterTypes();
    }
    
    @Override
    public Type $call$() {
        checkInit();
        return dispatch.$call$();
    }
    
    @Override
    public Type $callvariadic$() {
        return $callvariadic$(empty_.get_());
    }
    
    @Override
    public Type $callvariadic$(Sequential<?> varargs) {
        return $call$(varargs);
    }
    
    @Override
    public Type $call$(Object arg0) {
        checkInit();
        return dispatch.$call$(arg0);
    }
    
    @Override
    public Type $callvariadic$(Object arg0) {
        return $callvariadic$(arg0, empty_.get_());
    }
    
    @Override
    public Type $callvariadic$(Object arg0, Sequential<?> varargs) {
        return $call$(arg0, varargs);
    }
    
    @Override
    public Type $call$(Object arg0, Object arg1) {
        checkInit();
        return dispatch.$call$(arg0, arg1);
    }
    
    @Override
    public Type $callvariadic$(Object arg0, Object arg1) {
        return $callvariadic$(arg0, arg1, empty_.get_());
    }
    
    @Override
    public Type $callvariadic$(Object arg0, Object arg1, Sequential<?> varargs) {
        return $call$(arg0, arg1, varargs);
    }
    
    @Override
    public Type $call$(Object arg0, Object arg1, Object arg2) {
        checkInit();
        return dispatch.$call$(arg0, arg1, arg2);
    }
    
    @Override
    public Type $callvariadic$(Object arg0, Object arg1, Object arg2) {
        return $callvariadic$(arg0, arg1, arg2, empty_.get_());
    }
    
    @Override
    public Type $callvariadic$(Object arg0, Object arg1, Object arg2,
            Sequential<?> varargs) {
        return $call$(arg0, arg1, arg2, varargs);
    }
    
    @Override
    public Type $call$(Object... args) {
        checkInit();
        return dispatch.$call$(args);
    }
    
    @Override
    public Type $callvariadic$(Object... argsAndVarargs) {
        return $call$(argsAndVarargs);
    }
    
    @Override
    public short $getVariadicParameterIndex$() {
        checkInit();
        return dispatch.$getVariadicParameterIndex$();
    }
    
    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(CallableConstructorImpl.class, $reified$Type, $reified$Arguments);
    }
    
    public String toString() {
        return appliedClass.toString() + "." + freeConstructor.getName();
    }
    
    @Override
    public int hashCode() {
        return appliedClass.hashCode() ^ freeConstructor.getName().hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other instanceof CallableConstructorImpl) {
            CallableConstructorImpl<?,?> autre = (CallableConstructorImpl<?,?>)other;
            return this.appliedClass.equals(autre.appliedClass)
                    && freeConstructor.equals(autre.freeConstructor);
        } else {
            return false;
        }
    }
}

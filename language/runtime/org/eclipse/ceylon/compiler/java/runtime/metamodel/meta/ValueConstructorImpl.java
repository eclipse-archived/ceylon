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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.eclipse.ceylon.common.Nullable;
import org.eclipse.ceylon.compiler.java.Util;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.SatisfiedTypes;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.metadata.Variance;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.MethodHandleUtil;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ValueConstructorDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;
import org.eclipse.ceylon.model.loader.NamingBase;
import org.eclipse.ceylon.model.loader.impl.reflect.mirror.ReflectionClass;
import org.eclipse.ceylon.model.loader.model.JavaBeanValue;
import org.eclipse.ceylon.model.loader.model.LazyValue;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypedReference;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.ClassImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.ValueConstructorImpl;

import ceylon.language.null_;
import ceylon.language.meta.declaration.ValueConstructorDeclaration;
import ceylon.language.meta.model.IncompatibleTypeException;
import ceylon.language.meta.model.MutationException;
import ceylon.language.meta.model.StorageException;
import ceylon.language.meta.model.ValueConstructor;


@Ceylon(major=8)
@org.eclipse.ceylon.compiler.java.metadata.Class
@SatisfiedTypes("ceylon.language.meta.model::ValueConstructor<Get>")
@TypeParameters({
    @TypeParameter(value = "Get", variance = Variance.OUT),
})
public class ValueConstructorImpl<Get> 
        implements ValueConstructor<Get>, ReifiedType {

    private static final java.lang.Class<?>[] NO_PARAMS = new java.lang.Class<?>[0];
    
//    private final ceylon.language.meta.model.Type<Get> type;
    @Ignore
    protected final TypeDescriptor $reifiedGet;
    protected final ValueConstructorDeclarationImpl declaration;
    private MethodHandle getter;
    private final Object instance;
    
    protected final Type producedType;

    public final ClassImpl<Get,?> clazz;
    
    @Ignore
    public ValueConstructorImpl(TypeDescriptor $reifiedGet,
            ValueConstructorDeclarationImpl value,
            TypedReference valueTypedReference,
            ClassImpl<Get,?> clazz, Object instance) {
        this.producedType = valueTypedReference.getType();
//        this.type = Metamodel.getAppliedMetamodel(producedType);
        this.$reifiedGet = $reifiedGet;
        this.declaration = value;
        this.instance = instance;
        
        initField(instance, producedType);
        this.clazz = clazz;
    }

    @Override
    public ceylon.language.meta.model.Class<Get,?> getType() {
        return clazz;
    }
    
    @Override
    public ceylon.language.meta.model.Class<?,?> getContainer() {
        return null;
    }
    
    @Override
    public ceylon.language.meta.declaration.ValueConstructorDeclaration getDeclaration() {
        return (ValueConstructorDeclaration)declaration;
    }
    
    ///////////////////////////////////////////
    
    /**
     * Gets the getter {@code java.lang.reflect.Method} for the
     * given value constructor.
     */
    @Ignore
    public static Method getJavaMethod(ValueConstructorDeclarationImpl declaration) {
        org.eclipse.ceylon.model.typechecker.model.Value decl = (org.eclipse.ceylon.model.typechecker.model.Value) declaration.declaration;
        String getterName = "";
        try {
            if(decl instanceof JavaBeanValue){
                java.lang.Class<?> javaClass = Metamodel.getJavaClass((org.eclipse.ceylon.model.typechecker.model.ClassOrInterface)decl.getContainer());
                getterName = ((JavaBeanValue) decl).getGetterName();
                java.lang.Class<?>[] params = NO_PARAMS;
                boolean isJavaArray = MethodHandleUtil.isJavaArray(javaClass);
                if(isJavaArray)
                    params = MethodHandleUtil.getJavaArrayGetArrayParameterTypes(javaClass, getterName);
                // if it is shared we may want to get an inherited getter, but if it's private we need the declared method to return it
                Method m = decl.isShared() ? javaClass.getMethod(getterName, params) : javaClass.getDeclaredMethod(getterName, params);
                return m;
            }else if(decl instanceof LazyValue){
                LazyValue lazyDecl = (LazyValue) decl;
                java.lang.Class<?> javaClass = ((ReflectionClass)lazyDecl.classMirror).klass;
                // FIXME: we should really save the getter name in the LazyDecl
                getterName = NamingBase.getGetterName(lazyDecl);
                // toplevels don't have inheritance
                Method m = javaClass.getDeclaredMethod(getterName);
                return m;
            } else if (ModelUtil.isEnumeratedConstructor(decl)) {
                java.lang.Class<?> javaClass = Metamodel.getJavaClass((org.eclipse.ceylon.model.typechecker.model.ClassOrInterface)decl.getContainer());
                Class constructedClass = ModelUtil.getConstructedClass(decl);
                if (constructedClass.isMember()) {
                    // the getter for member classes is on the enclosing class.
                    javaClass = javaClass.getEnclosingClass();
                }else if(ModelUtil.isLocalNotInitializer(constructedClass))
                    return null; // local class has no way to get the value
                getterName = NamingBase.getGetterName(decl);
                
                java.lang.Class<?>[] params = NO_PARAMS;
                // if it is shared we may want to get an inherited getter, but if it's private we need the declared method to return it
                Method m = javaClass.getDeclaredMethod(getterName, params);
                return m;
            }else {
                throw new StorageException("Attribute "+decl.getName()+" is neither captured nor shared so it has no physical storage allocated and cannot be read by the metamodel");
            }
        } catch (NoSuchMethodException | SecurityException  e) {
            throw Metamodel.newModelError("Failed to find getter method "+getterName+" for: "+decl, e);
        }
    }
    
    private void initField(Object instance, Type valueType) {
        org.eclipse.ceylon.model.typechecker.model.Value decl = (org.eclipse.ceylon.model.typechecker.model.Value) declaration.declaration;
        Method m = getJavaMethod(declaration);
        // local classes have no getters
        if(m == null)
            return;
        String getterName = m.getName();
        try {
            if(decl instanceof JavaBeanValue){
                java.lang.Class<?> javaClass = Metamodel.getJavaClass((org.eclipse.ceylon.model.typechecker.model.ClassOrInterface)decl.getContainer());
                boolean isJavaArray = MethodHandleUtil.isJavaArray(javaClass);
                // if it is shared we may want to get an inherited getter, but if it's private we need the declared method to return it
                m.setAccessible(true);
                getter = MethodHandles.lookup().unreflect(m);
                java.lang.Class<?> getterType = m.getReturnType();
                getter = MethodHandleUtil.boxReturnValue(getter, getterType, valueType);
                if(instance != null 
                        // XXXArray.getArray is static but requires an instance as first param
                        && (isJavaArray || !Modifier.isStatic(m.getModifiers()))) {
                    getter = getter.bindTo(instance);
                }
                // we need to cast to Object because this is what comes out when calling it in $call
                getter = getter.asType(MethodType.methodType(Object.class));
            } else if (ModelUtil.isEnumeratedConstructor(decl)) {
                java.lang.Class<?> javaClass = Metamodel.getJavaClass((org.eclipse.ceylon.model.typechecker.model.ClassOrInterface)decl.getContainer());
                if (ModelUtil.getConstructedClass(decl).isMember()) {
                    // the getter for member classes is on the enclosing class.
                    javaClass = javaClass.getEnclosingClass();
                }
                // if it is shared we may want to get an inherited getter, but if it's private we need the declared method to return it
                m.setAccessible(true);
                getter = MethodHandles.lookup().unreflect(m);
                java.lang.Class<?> getterType = m.getReturnType();
                getter = MethodHandleUtil.boxReturnValue(getter, getterType, valueType);
                if(instance != null 
                        // XXXArray.getArray is static but requires an instance as first param
                        && (!Modifier.isStatic(m.getModifiers()))) {
                    getter = getter.bindTo(instance);
                }
                //if(!ModelUtil.getConstructedClass(decl).isMember()) {
                // we need to cast to Object because this is what comes out when calling it in $call
                getter = getter.asType(MethodType.methodType(Object.class));
                //}
            }else
                throw new StorageException("Attribute "+decl.getName()+" is neither captured nor shared so it has no physical storage allocated and cannot be read by the metamodel");
        
        } catch (SecurityException | IllegalAccessException e) {
            throw Metamodel.newModelError("Failed to find getter method "+getterName+" for: "+decl, e);
        }
    }

    @Override
    public Get get() {
        if($reifiedGet.equals(null_.$TypeDescriptor$))
            return null;
        if(getter == null)
            throw new StorageException("Attribute "+declaration.getName()+" is local so it has no physical storage allocated and cannot be read by the metamodel");
        try {
            return (Get) getter.invokeExact();
        } catch (Throwable e) {
            Util.rethrow(e);
            return null;
        }
    }

    @Override
    public Object set(java.lang.Object value) {
        throw new MutationException("Value is not mutable");
    }

    @Override
    public java.lang.Object $setIfAssignable(@Name("newValue") @TypeInfo("ceylon.language::Anything") @Nullable java.lang.Object newValue){
        Type newValueType = Metamodel.getProducedType(newValue);
        if(!newValueType.isSubtypeOf(this.producedType))
            throw new IncompatibleTypeException("Invalid new value type: "+newValueType+", expecting: "+this.producedType);
        return set(newValue);
    }

    @Override
    public int hashCode() {
        int result = 1;
        // in theory, if our instance is the same, our containing type should be the same
        // and if we don't have an instance we're a toplevel and have no containing type
        result = 37 * result + (instance == null ? 0 : instance.hashCode());
        result = 37 * result + getDeclaration().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ValueConstructorImpl == false)
            return false;
        ValueConstructorImpl<?> other = (ValueConstructorImpl<?>) obj;
        // in theory, if our instance is the same, our containing type should be the same
        // and if we don't have an instance we're a toplevel and have no containing type
        return Util.eq(instance, other.instance)
                && getDeclaration().equals(other.getDeclaration());
    }

    @Override
    public String toString() {
        return Metamodel.toTypeString(this);
    }


    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(ValueConstructorImpl.class, $reifiedGet);
    }

    

}
package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

import ceylon.language.metamodel.AppliedDeclaration$impl;
import ceylon.language.metamodel.AppliedProducedType;
import ceylon.language.metamodel.AppliedProducedType$impl;
import ceylon.language.metamodel.AppliedValue$impl;
import ceylon.language.metamodel.Declaration;

import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.loader.impl.reflect.mirror.ReflectionClass;
import com.redhat.ceylon.compiler.loader.model.JavaBeanValue;
import com.redhat.ceylon.compiler.loader.model.LazyValue;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

public class AppliedValue<Type> implements ceylon.language.metamodel.AppliedValue<Type>, ReifiedType {

    private AppliedProducedType type;
    @Ignore
    protected TypeDescriptor $reifiedType;
    protected Value declaration;
    private MethodHandle getter;

    public AppliedValue(Value value, ProducedType valueType, AppliedClassOrInterfaceType<Type> appliedClassOrInterfaceType) {
        this.type = Metamodel.getAppliedMetamodel(valueType);
        this.$reifiedType = Metamodel.getTypeDescriptorForProducedType(valueType);
        this.declaration = value;
        
        initField(appliedClassOrInterfaceType);
    }

    private void initField(AppliedClassOrInterfaceType<Type> appliedClassOrInterfaceType) {
        com.redhat.ceylon.compiler.typechecker.model.Declaration decl = declaration.declaration;
        if(decl instanceof JavaBeanValue){
            java.lang.Class<?> javaClass = Metamodel.getJavaClass(appliedClassOrInterfaceType.declaration.declaration);
            String getterName = ((JavaBeanValue) decl).getGetterName();
            try {
                Method m = javaClass.getMethod(getterName);
                getter = MethodHandles.lookup().unreflect(m);
                java.lang.Class<?> getterType = m.getReturnType();
                getter = MethodHandleUtil.boxReturnValue(getter, getterType);
                // we need to cast to Object because this is what comes out when calling it in $call
                getter = getter.asType(MethodType.methodType(Object.class, Object.class));

                initField(decl, javaClass, getterType);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Failed to find getter method "+getterName+" for: "+decl, e);
            } catch (SecurityException e) {
                throw new RuntimeException("Failed to find getter method "+getterName+" for: "+decl, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to find getter method "+getterName+" for: "+decl, e);
            }
        }else if(decl instanceof LazyValue){
            LazyValue lazyDecl = (LazyValue) decl;
            java.lang.Class<?> javaClass = ((ReflectionClass)lazyDecl.classMirror).klass;
            // FIXME: we should really save the getter name in the LazyDecl
            String getterName = Naming.getGetterName(lazyDecl);
            try {
                Method m = javaClass.getMethod(getterName);
                getter = MethodHandles.lookup().unreflect(m);
                java.lang.Class<?> getterType = m.getReturnType();
                getter = MethodHandleUtil.boxReturnValue(getter, getterType);
                // we need to cast to Object because this is what comes out when calling it in $call
                getter = getter.asType(MethodType.methodType(Object.class));

//                initField(decl, javaClass, getterType);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Failed to find getter method "+getterName+" for: "+decl, e);
            } catch (SecurityException e) {
                throw new RuntimeException("Failed to find getter method "+getterName+" for: "+decl, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to find getter method "+getterName+" for: "+decl, e);
            }
        }else
            throw new RuntimeException("Unsupported attribute type: "+decl);
    }

    // for AppliedVariable
    protected void initField(com.redhat.ceylon.compiler.typechecker.model.Declaration decl, java.lang.Class<?> javaClass, java.lang.Class<?> getterReturnType) {}

    @Override
    @Ignore
    public AppliedProducedType$impl $ceylon$language$metamodel$AppliedProducedType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public AppliedValue$impl<Type> $ceylon$language$metamodel$AppliedValue$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public AppliedDeclaration$impl $ceylon$language$metamodel$AppliedDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Declaration")
    public Declaration getDeclaration() {
        return declaration;
    }

    @Override
    public Type get(@Name("instance") @TypeInfo("ceylon.language::Object") Object instance) {
        try {
            if(declaration.getToplevel())
                return (Type) getter.invokeExact();
            return (Type) getter.invokeExact(instance);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to invoke getter for "+declaration.getName(), e);
        }
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::AppliedProducedType")
    public AppliedProducedType getType() {
        return type;
    }

    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedValue.class, $reifiedType);
    }

}

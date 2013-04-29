package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;

import ceylon.language.metamodel.AppliedDeclaration$impl;
import ceylon.language.metamodel.AppliedProducedType;
import ceylon.language.metamodel.AppliedProducedType$impl;
import ceylon.language.metamodel.AppliedValue$impl;
import ceylon.language.metamodel.Declaration;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.loader.model.JavaBeanValue;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

public class AppliedValue<Type> implements ceylon.language.metamodel.AppliedValue<Type>, ReifiedType {

    private AppliedProducedType type;
    private TypeDescriptor $reifiedType;
    private Value declaration;
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
            String getterName = ((JavaBeanValue) decl).getGetterName();
            java.lang.Class<?> javaClass = Metamodel.getJavaClass(appliedClassOrInterfaceType.declaration.declaration);
            try {
                Method m = javaClass.getMethod(getterName);
                getter = MethodHandles.lookup().unreflect(m);
                java.lang.Class<?> paramType = m.getReturnType();
                // FIXME: more boxing
                if(paramType == java.lang.String.class){
                    // ceylon.language.String.instance(obj)
                    MethodHandle box = MethodHandles.lookup().findStatic(ceylon.language.String.class, "instance", 
                                                                         MethodType.methodType(ceylon.language.String.class, java.lang.String.class));
                    getter = MethodHandles.filterReturnValue(getter, box);
                }else if(paramType == long.class){
                    // ceylon.language.Integer.instance(obj)
                    MethodHandle box = MethodHandles.lookup().findStatic(ceylon.language.Integer.class, "instance", 
                                                                         MethodType.methodType(ceylon.language.Integer.class, long.class));
                    getter = MethodHandles.filterReturnValue(getter, box);
                }
                // we need to cast to Object because this is what comes out when calling it in $call
                getter = getter.asType(MethodType.methodType(Object.class, Object.class));
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

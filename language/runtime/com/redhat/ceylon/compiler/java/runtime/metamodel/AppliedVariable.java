package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

import ceylon.language.metamodel.AppliedVariable$impl;

import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.loader.model.JavaBeanValue;
import com.redhat.ceylon.compiler.loader.model.LazyValue;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

public class AppliedVariable<Type> extends AppliedValue<Type> implements ceylon.language.metamodel.AppliedVariable<Type> {

    private MethodHandle setter;

    public AppliedVariable(Value value, ProducedType valueType, AppliedClassOrInterfaceType<Type> appliedClassOrInterfaceType) {
        super(value, valueType, appliedClassOrInterfaceType);
    }

    @Override
    protected void initField(com.redhat.ceylon.compiler.typechecker.model.Declaration decl, java.lang.Class<?> javaClass, java.lang.Class<?> getterReturnType) {
        if(decl instanceof JavaBeanValue){
            String setterName = ((JavaBeanValue) decl).getSetterName();
            try {
                Method m = javaClass.getMethod(setterName, getterReturnType);
                setter = MethodHandles.lookup().unreflect(m);
                setter = setter.asType(MethodType.methodType(void.class, Object.class, getterReturnType));
                setter = MethodHandleUtil.unboxArguments(setter, 0, 1, new java.lang.Class[]{getterReturnType});
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Failed to find setter method "+setterName+" for: "+decl, e);
            } catch (SecurityException e) {
                throw new RuntimeException("Failed to find setter method "+setterName+" for: "+decl, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to find setter method "+setterName+" for: "+decl, e);
            }
        }else if(decl instanceof LazyValue){
            // FIXME: we should really save the getter name in the LazyDecl
            String setterName = Naming.getSetterName(decl);
            try {
                Method m = javaClass.getMethod(setterName, getterReturnType);
                setter = MethodHandles.lookup().unreflect(m);
                setter = setter.asType(MethodType.methodType(void.class, getterReturnType));
                setter = MethodHandleUtil.unboxArguments(setter, 0, 0, new java.lang.Class[]{getterReturnType});
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Failed to find setter method "+setterName+" for: "+decl, e);
            } catch (SecurityException e) {
                throw new RuntimeException("Failed to find setter method "+setterName+" for: "+decl, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to find setter method "+setterName+" for: "+decl, e);
            }
        }else
            throw new RuntimeException("Unsupported attribute type: "+decl);
    }

    @Override
    @Ignore
    public AppliedVariable$impl<Type> $ceylon$language$metamodel$AppliedVariable$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object set(@Name("instance") @TypeInfo("ceylon.language::Object") Object instance, Type value) {
        try {
            if(declaration.getToplevel())
                setter.invokeExact(value);
            else
                setter.invokeExact(instance, value);
            return null;
        } catch (Throwable e) {
            throw new RuntimeException("Failed to invoke setter for "+declaration.getName(), e);
        }
    }

    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedVariable.class, $reifiedType);
    }

}

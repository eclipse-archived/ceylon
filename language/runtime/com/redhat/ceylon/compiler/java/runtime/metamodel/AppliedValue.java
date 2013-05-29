package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

import ceylon.language.metamodel.DeclarationType$impl;
import ceylon.language.metamodel.Attribute$impl;

import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.loader.impl.reflect.mirror.ReflectionClass;
import com.redhat.ceylon.compiler.loader.model.JavaBeanValue;
import com.redhat.ceylon.compiler.loader.model.LazyValue;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

public class AppliedValue<Type> 
        implements ceylon.language.metamodel.Attribute<Type>, ReifiedType {

    private ceylon.language.metamodel.Type type;
    @Ignore
    protected TypeDescriptor $reifiedType;
    protected FreeAttribute declaration;
    private MethodHandle getter;

    public AppliedValue(FreeAttribute value, ProducedType valueType, Object instance) {
        this.type = Metamodel.getAppliedMetamodel(valueType);
        this.$reifiedType = Metamodel.getTypeDescriptorForProducedType(valueType);
        this.declaration = value;
        
        initField(instance);
    }

    private void initField(Object instance) {
        com.redhat.ceylon.compiler.typechecker.model.Declaration decl = declaration.declaration;
        if(decl instanceof JavaBeanValue){
            java.lang.Class<?> javaClass = Metamodel.getJavaClass((com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface)decl.getContainer());
            String getterName = ((JavaBeanValue) decl).getGetterName();
            try {
                Method m = javaClass.getMethod(getterName);
                getter = MethodHandles.lookup().unreflect(m);
                java.lang.Class<?> getterType = m.getReturnType();
                getter = MethodHandleUtil.boxReturnValue(getter, getterType);
                getter = getter.bindTo(instance);
                // we need to cast to Object because this is what comes out when calling it in $call
                getter = getter.asType(MethodType.methodType(Object.class));

                initField(decl, javaClass, getterType, instance);
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

                initField(decl, javaClass, getterType, null);
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
    protected void initField(com.redhat.ceylon.compiler.typechecker.model.Declaration decl, java.lang.Class<?> javaClass, java.lang.Class<?> getterReturnType, Object instance) {}

    @Override
    @Ignore
    public Attribute$impl<Type> $ceylon$language$metamodel$Attribute$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public DeclarationType$impl $ceylon$language$metamodel$DeclarationType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.declaration::AttributeDeclaration")
    public ceylon.language.metamodel.declaration.AttributeDeclaration getDeclaration() {
        return declaration;
    }

    @Override
    public Type get() {
        try {
            return (Type) getter.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException("Failed to invoke getter for "+declaration.getName(), e);
        }
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Type")
    public ceylon.language.metamodel.Type getType() {
        return type;
    }

    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedValue.class, $reifiedType);
    }

}

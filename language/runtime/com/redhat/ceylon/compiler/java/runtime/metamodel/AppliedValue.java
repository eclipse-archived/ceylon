package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import ceylon.language.model.Model$impl;
import ceylon.language.model.Value$impl;
import ceylon.language.model.ValueModel$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.loader.impl.reflect.mirror.ReflectionClass;
import com.redhat.ceylon.compiler.loader.model.FieldValue;
import com.redhat.ceylon.compiler.loader.model.JavaBeanValue;
import com.redhat.ceylon.compiler.loader.model.LazyValue;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
})
public class AppliedValue<Type> 
        implements ceylon.language.model.Value<Type>, ReifiedType {

    private ceylon.language.model.Type<Type> type;
    @Ignore
    protected TypeDescriptor $reifiedType;
    protected FreeAttribute declaration;
    private MethodHandle getter;
    private Object instance;
    private ceylon.language.model.Type<? extends java.lang.Object> container;

    public AppliedValue(@Ignore TypeDescriptor $reifiedType, FreeAttribute value, ProducedTypedReference valueTypedReference, 
            ceylon.language.model.Type<?> container, Object instance) {
        ProducedType producedType = valueTypedReference.getType();
        this.container = container;
        this.type = Metamodel.getAppliedMetamodel(producedType);
        this.$reifiedType = $reifiedType;
        this.declaration = value;
        this.instance = instance;
        
        initField(instance, producedType);
    }

    private void initField(Object instance, ProducedType valueType) {
        com.redhat.ceylon.compiler.typechecker.model.Declaration decl = declaration.declaration;
        if(decl instanceof JavaBeanValue){
            java.lang.Class<?> javaClass = Metamodel.getJavaClass((com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface)decl.getContainer());
            if(javaClass == ceylon.language.Object.class
                    || javaClass == ceylon.language.Basic.class
                    || javaClass == ceylon.language.Identifiable.class){
                if("string".equals(decl.getName())
                        || "hash".equals(decl.getName())){
                    // look it up on j.l.Object, getterName should work
                    javaClass = java.lang.Object.class;
                }else{
                    throw new RuntimeException("Object/Basic/Identifiable member not supported: "+decl.getName());
                }
            }
            String getterName = ((JavaBeanValue) decl).getGetterName();
            try {
                // if it is shared we may want to get an inherited getter, but if it's private we need the declared method to return it
                Method m = decl.isShared() ? javaClass.getMethod(getterName) : javaClass.getDeclaredMethod(getterName);
                m.setAccessible(true);
                getter = MethodHandles.lookup().unreflect(m);
                java.lang.Class<?> getterType = m.getReturnType();
                getter = MethodHandleUtil.boxReturnValue(getter, getterType, valueType);
                getter = getter.bindTo(instance);
                // we need to cast to Object because this is what comes out when calling it in $call
                getter = getter.asType(MethodType.methodType(Object.class));

                initField(decl, javaClass, getterType, instance, valueType);
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
                // toplevels don't have inheritance
                Method m = javaClass.getDeclaredMethod(getterName);
                m.setAccessible(true);
                getter = MethodHandles.lookup().unreflect(m);
                java.lang.Class<?> getterType = m.getReturnType();
                getter = MethodHandleUtil.boxReturnValue(getter, getterType, valueType);
                // we need to cast to Object because this is what comes out when calling it in $call
                getter = getter.asType(MethodType.methodType(Object.class));

                initField(decl, javaClass, getterType, null, valueType);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Failed to find getter method "+getterName+" for: "+decl, e);
            } catch (SecurityException e) {
                throw new RuntimeException("Failed to find getter method "+getterName+" for: "+decl, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to find getter method "+getterName+" for: "+decl, e);
            }
        }else if(decl instanceof FieldValue){
            FieldValue fieldDecl = (FieldValue) decl;
            java.lang.Class<?> javaClass = Metamodel.getJavaClass((com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface)decl.getContainer());
            String fieldName = fieldDecl.getRealName();
            try {
                // fields are not inherited
                Field f = javaClass.getDeclaredField(fieldName);
                f.setAccessible(true);
                getter = MethodHandles.lookup().unreflectGetter(f);
                java.lang.Class<?> getterType = f.getType();
                getter = MethodHandleUtil.boxReturnValue(getter, getterType, valueType);
                getter = getter.bindTo(instance);
                // we need to cast to Object because this is what comes out when calling it in $call
                getter = getter.asType(MethodType.methodType(Object.class));

                initField(decl, javaClass, getterType, instance, valueType);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Failed to find field "+fieldName+" for: "+decl, e);
            } catch (SecurityException e) {
                throw new RuntimeException("Failed to find field "+fieldName+" for: "+decl, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to find field "+fieldName+" for: "+decl, e);
            }
        }else
            throw new RuntimeException("Unsupported attribute type: "+decl);
    }

    // for AppliedVariable
    protected void initField(com.redhat.ceylon.compiler.typechecker.model.Declaration decl, java.lang.Class<?> javaClass, 
                             java.lang.Class<?> getterReturnType, Object instance, ProducedType valueType) {}

    @Override
    @Ignore
    public Value$impl<Type> $ceylon$language$model$Value$impl() {
        return null;
    }

    @Override
    @Ignore
    public ValueModel$impl<Type> $ceylon$language$model$ValueModel$impl() {
        return null;
    }

    @Override
    @Ignore
    public Model$impl $ceylon$language$model$Model$impl() {
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.model.declaration::ValueDeclaration")
    public ceylon.language.model.declaration.ValueDeclaration getDeclaration() {
        return declaration;
    }

    @Override
    public Type get() {
        try {
            return (Type) getter.invokeExact();
        } catch (Throwable e) {
            Util.rethrow(e);
            return null;
        }
    }

    @Override
    @TypeInfo("ceylon.language.model::Type<Type>")
    public ceylon.language.model.Type<? extends Type> getType() {
        return type;
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
        if(obj instanceof AppliedValue == false)
            return false;
        AppliedValue<?> other = (AppliedValue<?>) obj;
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
    @TypeInfo("ceylon.language.model::Type<ceylon.language::Anything>")
    public ceylon.language.model.Type<? extends java.lang.Object> getContainer(){
        return container;
    }

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedValue.class, $reifiedType);
    }

}

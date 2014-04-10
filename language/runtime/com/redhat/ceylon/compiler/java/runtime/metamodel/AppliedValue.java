package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import ceylon.language.meta.model.IncompatibleTypeException;
import ceylon.language.meta.model.Model$impl;
import ceylon.language.meta.model.MutationException;
import ceylon.language.meta.model.Value$impl;
import ceylon.language.meta.model.ValueModel$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
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

@Ceylon(major = 7)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Get", variance = Variance.OUT),
    @TypeParameter(value = "Set", variance = Variance.IN),
})
public class AppliedValue<Get, Set> 
        implements ceylon.language.meta.model.Value<Get, Set>, ReifiedType {

    private static final Class<?>[] NO_PARAMS = new Class<?>[0];
    
    private ceylon.language.meta.model.Type<Get> type;
    @Ignore
    protected TypeDescriptor $reifiedGet;
    @Ignore
    protected TypeDescriptor $reifiedSet;
    protected FreeValue declaration;
    private MethodHandle getter;
    private MethodHandle setter;
    private Object instance;
    private ceylon.language.meta.model.Type<?> container;
    protected ProducedType producedType;

    public AppliedValue(@Ignore TypeDescriptor $reifiedGet, @Ignore TypeDescriptor $reifiedSet,
            FreeValue value, ProducedTypedReference valueTypedReference, 
            ceylon.language.meta.model.Type<?> container, Object instance) {
        this.producedType = valueTypedReference.getType();
        this.container = container;
        this.type = Metamodel.getAppliedMetamodel(producedType);
        this.$reifiedGet = $reifiedGet;
        this.$reifiedSet = $reifiedSet;
        this.declaration = value;
        this.instance = instance;
        
        initField(instance, producedType);
    }

    private void initField(Object instance, ProducedType valueType) {
        com.redhat.ceylon.compiler.typechecker.model.Value decl = (com.redhat.ceylon.compiler.typechecker.model.Value) declaration.declaration;
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
                Class<?>[] params = NO_PARAMS;
                boolean isJavaArray = MethodHandleUtil.isJavaArray(javaClass);
                if(isJavaArray)
                    params = MethodHandleUtil.getJavaArrayGetArrayParameterTypes(javaClass, getterName);
                // if it is shared we may want to get an inherited getter, but if it's private we need the declared method to return it
                Method m = decl.isShared() ? javaClass.getMethod(getterName, params) : javaClass.getDeclaredMethod(getterName, params);
                m.setAccessible(true);
                getter = MethodHandles.lookup().unreflect(m);
                java.lang.Class<?> getterType = m.getReturnType();
                getter = MethodHandleUtil.boxReturnValue(getter, getterType, valueType);
                if(instance != null 
                        // XXXArray.getArray is static but requires an instance as first param
                        && (isJavaArray || !Modifier.isStatic(m.getModifiers())))
                    getter = getter.bindTo(instance);
                // we need to cast to Object because this is what comes out when calling it in $call
                getter = getter.asType(MethodType.methodType(Object.class));

                initSetter(decl, javaClass, getterType, instance, valueType);
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

                initSetter(decl, javaClass, getterType, null, valueType);
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
            if(MethodHandleUtil.isJavaArray(javaClass)){
                try {
                    Method method = Array.class.getDeclaredMethod("getLength", Object.class);
                    getter = MethodHandles.lookup().unreflect(method);
                    java.lang.Class<?> getterType = method.getReturnType();
                    getter = MethodHandleUtil.boxReturnValue(getter, getterType, valueType);
                    // this one is static but requires an instance a first param
                    if(instance != null)
                        getter = getter.bindTo(instance);
                    // we need to cast to Object because this is what comes out when calling it in $call
                    getter = getter.asType(MethodType.methodType(Object.class));
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Failed to find Array.getLength method for: "+decl, e);
                } catch (SecurityException e) {
                    throw new RuntimeException("Failed to find Array.getLength method for: "+decl, e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to find Array.getLength method for: "+decl, e);
                }
            }else{
                try {
                    // fields are not inherited
                    Field f = javaClass.getDeclaredField(fieldName);
                    f.setAccessible(true);
                    getter = MethodHandles.lookup().unreflectGetter(f);
                    java.lang.Class<?> getterType = f.getType();
                    getter = MethodHandleUtil.boxReturnValue(getter, getterType, valueType);
                    if(instance != null && !Modifier.isStatic(f.getModifiers()))
                        getter = getter.bindTo(instance);
                    // we need to cast to Object because this is what comes out when calling it in $call
                    getter = getter.asType(MethodType.methodType(Object.class));

                    initSetter(decl, javaClass, getterType, instance, valueType);
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException("Failed to find field "+fieldName+" for: "+decl, e);
                } catch (SecurityException e) {
                    throw new RuntimeException("Failed to find field "+fieldName+" for: "+decl, e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to find field "+fieldName+" for: "+decl, e);
                }
            }
        }else
            throw new RuntimeException("Unsupported attribute type: "+decl);
    }

    private void initSetter(com.redhat.ceylon.compiler.typechecker.model.Value decl, java.lang.Class<?> javaClass, 
                            java.lang.Class<?> getterReturnType, Object instance, ProducedType valueType) {
        if(!decl.isVariable())
            return;
        if(decl instanceof JavaBeanValue){
            String setterName = ((JavaBeanValue) decl).getSetterName();
            try {
                Method m = javaClass.getMethod(setterName, getterReturnType);
                m.setAccessible(true);
                setter = MethodHandles.lookup().unreflect(m);
                if(instance != null && !Modifier.isStatic(m.getModifiers()))
                    setter = setter.bindTo(instance);
                setter = setter.asType(MethodType.methodType(void.class, getterReturnType));
                setter = MethodHandleUtil.unboxArguments(setter, 0, 0, new java.lang.Class[]{getterReturnType}, Arrays.asList(valueType));
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
                m.setAccessible(true);
                setter = MethodHandles.lookup().unreflect(m);
                setter = setter.asType(MethodType.methodType(void.class, getterReturnType));
                setter = MethodHandleUtil.unboxArguments(setter, 0, 0, new java.lang.Class[]{getterReturnType}, Arrays.asList(valueType));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Failed to find setter method "+setterName+" for: "+decl, e);
            } catch (SecurityException e) {
                throw new RuntimeException("Failed to find setter method "+setterName+" for: "+decl, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to find setter method "+setterName+" for: "+decl, e);
            }
        }else if(decl instanceof FieldValue){
            String fieldName = ((FieldValue) decl).getRealName();
            try {
                Field f = javaClass.getField(fieldName);
                f.setAccessible(true);
                setter = MethodHandles.lookup().unreflectSetter(f);
                if(instance != null && !Modifier.isStatic(f.getModifiers()))
                    setter = setter.bindTo(instance);
                setter = setter.asType(MethodType.methodType(void.class, getterReturnType));
                setter = MethodHandleUtil.unboxArguments(setter, 0, 0, new java.lang.Class[]{getterReturnType}, Arrays.asList(valueType));
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

    @Override
    @Ignore
    public Value$impl<Get,Set> $ceylon$language$meta$model$Value$impl() {
        return null;
    }

    @Override
    @Ignore
    public ValueModel$impl<Get,Set> $ceylon$language$meta$model$ValueModel$impl() {
        return null;
    }

    @Override
    @Ignore
    public Model$impl $ceylon$language$meta$model$Model$impl() {
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::ValueDeclaration")
    public ceylon.language.meta.declaration.ValueDeclaration getDeclaration() {
        return declaration;
    }

    @Override
    public Get get() {
        try {
            return (Get) getter.invokeExact();
        } catch (Throwable e) {
            Util.rethrow(e);
            return null;
        }
    }

    @Override
    public Object set(Set value) {
        if(!declaration.getVariable())
            throw new MutationException("Value is not mutable");
        try {
            setter.invokeExact(value);
            return null;
        } catch (Throwable e) {
            Util.rethrow(e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public java.lang.Object $setIfAssignable(@Name("newValue") @TypeInfo("ceylon.language::Anything") java.lang.Object newValue){
        ProducedType newValueType = Metamodel.getProducedType(newValue);
        if(!newValueType.isSubtypeOf(this.producedType))
            throw new IncompatibleTypeException("Invalid new value type: "+newValueType+", expecting: "+this.producedType);
        return set((Set) newValue);
    }

    @Override
    @TypeInfo("ceylon.language.meta.model::Type<Get>")
    public ceylon.language.meta.model.Type<? extends Get> getType() {
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
        AppliedValue<?,?> other = (AppliedValue<?,?>) obj;
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
    @TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>|ceylon.language::Null")
    public ceylon.language.meta.model.Type<?> getContainer(){
        return container;
    }

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(AppliedValue.class, $reifiedGet, $reifiedSet);
    }
}

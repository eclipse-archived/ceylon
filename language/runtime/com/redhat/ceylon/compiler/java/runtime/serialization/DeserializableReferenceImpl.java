package com.redhat.ceylon.compiler.java.runtime.serialization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedClass;
import com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedMemberClass;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

import ceylon.language.AssertionError;
import ceylon.language.meta.model.ClassModel;
import ceylon.language.serialization.DeserializableReference;
import ceylon.language.serialization.RealizableReference;
import ceylon.language.serialization.Deconstructed;

class DeserializableReferenceImpl<Instance> 
        implements DeserializableReference<Instance>, $InstanceLeaker$<Instance>, ReifiedType {
    
    private final TypeDescriptor reified$Instance;
    private final DeserializationContextImpl context;
    private final Object id;
    private final Instance instance;
    private final ClassModel classModel;
    
    DeserializableReferenceImpl(TypeDescriptor reified$Instance, 
            DeserializationContextImpl context, 
            Object id, 
            @SuppressWarnings("rawtypes") ClassModel classModel) {
        this.reified$Instance = reified$Instance;
        this.context = context;
        this.id = id;
        this.classModel = classModel;
        java.lang.Class<Instance> clazz;
        TypeDescriptor[] typeArguments = ((TypeDescriptor.Class)((ReifiedType)classModel).$getType$()).getTypeArguments();
        if (classModel instanceof AppliedClass) {
            clazz = (java.lang.Class)((TypeDescriptor.Class)typeArguments[0]).getKlass();
        } else if (classModel instanceof AppliedMemberClass) {
            clazz = (java.lang.Class)((TypeDescriptor.Class)typeArguments[1]).getKlass();
        } else {
            throw new AssertionError("unexpected class model: " 
                    + (classModel != null ? classModel.getClass().getName() : "null"));
        }
        
        try {
            Constructor<Instance> ctor = clazz.getDeclaredConstructor($Serialization$.class);
            ctor.setAccessible(true);
            instance = ctor.newInstance(new Object[]{null});// Pass a null $Serialization$
            context.put(id, (DeserializableReferenceImpl)this);
        } catch (NoSuchMethodException e) {
            throw new AssertionError("class is not serializable " + classModel);
        } catch (InvocationTargetException e) {
            throw new AssertionError("error thrown during instantiation of " + classModel+ (e.getMessage() != null ? ": " + e.getMessage() : ""));
        } catch (SecurityException e) {
            // Should never happen
            throw new RuntimeException(e);
        } catch (InstantiationException|IllegalAccessException|IllegalArgumentException e) {
            // Should never happen
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Object getId() {
        return id;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public ClassModel getClazz() {
        return classModel;
    }
    
    /**
     * Instantiates and returns a {@link DeserializingStatefulReference}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public RealizableReference<Instance> deserialize(Deconstructed deconstructed) {
        RealizableReferenceImpl result = new RealizableReferenceImpl<Instance>(reified$Instance, context, id, classModel, instance, deconstructed);
        context.update(id, result);
        return result;
    }
    
    public String toString() {
        return id +"<-"+classModel;
    }

    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(DeserializableReferenceImpl.class, reified$Instance);
    }

    @Override
    public Instance $leakInstance$() {
        return instance;
    }
    
}
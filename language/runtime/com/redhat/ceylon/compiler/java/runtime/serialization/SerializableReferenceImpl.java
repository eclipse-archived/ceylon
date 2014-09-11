package com.redhat.ceylon.compiler.java.runtime.serialization;

import ceylon.language.Callable;
import ceylon.language.meta.type_;
import ceylon.language.meta.model.ClassModel;
import ceylon.language.serialization.Deconstructor;
import ceylon.language.serialization.SerializableReference;

import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

class SerializableReferenceImpl<Instance> 
implements SerializableReference<Instance>, ReifiedType {
    private final TypeDescriptor reified$Instance;
    private final Object id;
    private final Instance instance;

    SerializableReferenceImpl(TypeDescriptor reified$Instance, SerializationContextImpl context, Object id, Instance instance) {
        this.reified$Instance = reified$Instance;
        this.id = id;
        this.instance = instance;
    }

    public String toString() {
        return id +"=>" + instance;
    }

    @Override
    public /*Deconstructed<Instance>*/ Object serialize(Callable<? extends Deconstructor> deconstructor) {
        if (this.instance instanceof Serializable) {
            ((Serializable)this.instance).$serialize$(deconstructor);
            return null;
        } else {
            throw new ceylon.language.AssertionError("object is not an instance of a serializable class");
        }
    }

    @Override
    public Instance instance() {
        return instance;
    }

    /** 
     * Construct the instance (by calling its constructor) and set 
     * its value-typed attributes. 
     * Does not set the reference-typed attributes
     *//*
@Override
public Object reconstruct() {
// XXX no op because we're for serialization and this only makes sense for deserialization?
return null;
}*/


    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(SerializableReferenceImpl.class, reified$Instance);
    }

    @Override
    public Object getId() {
        return id;
    }


    @SuppressWarnings("rawtypes")
    @Override
    public ClassModel getClazz() {
        return type_.type(reified$Instance, instance);
    }
}
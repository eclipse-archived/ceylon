package com.redhat.ceylon.compiler.java.runtime.serialization;

import ceylon.language.AssertionError;
import ceylon.language.meta.type_;
import ceylon.language.serialization.References;
import ceylon.language.serialization.SerializationContext;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

/**
 * The implementation of {@link SerializationContext}
 */
@Ceylon(major = 8)
@Class
@SatisfiedTypes("ceylon.language.serialization::SerializationContext")
public class SerializationContextImpl  
        implements SerializationContext, ReifiedType {
    
    
    public References references(java.lang.Object instance) {
        if (instance instanceof Serializable) {
            return new ReferencesImpl((Serializable)instance);
        } else {
            throw new AssertionError("instance is not serializable " + type_.type(TypeDescriptor.NothingType, instance));
        }
    }
    
    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(SerializationContextImpl.class);
    }
}

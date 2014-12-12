package com.redhat.ceylon.compiler.java.runtime.serialization;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import ceylon.language.Iterator;
import ceylon.language.Null;
import ceylon.language.finished_;
import ceylon.language.impl.BaseIterable;
import ceylon.language.serialization.SerializationContext;
import ceylon.language.serialization.SerializableReference;

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
        extends BaseIterable<SerializableReference<Object>, Object> 
        implements SerializationContext, ReifiedType {
    
    private final IdentityHashMap<Object, SerializableReference<?>> identifiableToReference = new IdentityHashMap<>();
    private final HashMap<Object, SerializableReference<?>> unidentifiableToReference = new HashMap<>();
    
    private Map<Object, SerializableReference<?>> map(Object instance) {
        if (instance instanceof ceylon.language.Identifiable) {
            return identifiableToReference;
        } else {
            return unidentifiableToReference;
        }
    }
    
    public SerializationContextImpl() {
        super(TypeDescriptor.klass(SerializableReferenceImpl.class, ceylon.language.Object.$TypeDescriptor$), Null.$TypeDescriptor$);
    }
    
    /**
     * "Create a reference to the given [[instance]] of 
     [[Class]], assigning it the given [[identifer|id]]."
     @throws Exception "if there is already an instance with the given
         identifier"
     */
    @Override
    public <Instance> SerializableReference<Instance> reference(TypeDescriptor reified$Instance, Object id, Instance instance) {
        SerializableReferenceImpl ref = new SerializableReferenceImpl(reified$Instance, this, id, instance);
        SerializableReference<?> prevReference = map(instance).put(instance, ref);
        if (prevReference != null) {
            throw new ceylon.language.AssertionError("An instance has already been registered with id "+id+": \"" + prevReference.instance() +"\", \""+ instance+"\"");
        }
        return ref;
    }
    
    @Override
    public <Instance> SerializableReference<Instance> getReference(TypeDescriptor reified$Instance, Instance instance) {
        SerializableReference<?> ref = map(instance).get(instance);
        if (ref == null) {
            return null;
        }
        return (SerializableReference)ref;
    }

    @Override
    public Iterator<? extends SerializableReference<Object>> iterator() {
        return new Iterator<SerializableReference<Object>>() {
            private final java.util.Iterator<SerializableReference<?>> identifiableIter = identifiableToReference.values().iterator();
            private final java.util.Iterator<SerializableReference<?>> unidentifiableIter = unidentifiableToReference.values().iterator();
            @Override
            public Object next() {
                if (identifiableIter.hasNext()) {
                    return identifiableIter.next();
                } else if (unidentifiableIter.hasNext()) {
                    return unidentifiableIter.next();
                } else {
                    return finished_.get_();
                }
            }
        };
    }
    
    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(SerializationContextImpl.class);
    }
}

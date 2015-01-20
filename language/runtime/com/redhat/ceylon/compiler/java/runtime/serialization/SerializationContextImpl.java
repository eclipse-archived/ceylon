package com.redhat.ceylon.compiler.java.runtime.serialization;

import java.util.HashMap;
import java.util.IdentityHashMap;

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
    
    private final IdentityHashMap<Object, Object> instanceToId = new IdentityHashMap<>();
    private final HashMap<Object, SerializableReference<Object>> idToReference = new HashMap<Object, SerializableReference<Object>>();
    
    public SerializationContextImpl() {
        super(TypeDescriptor.klass(SerializableReferenceImpl.class, ceylon.language.Object.$TypeDescriptor$), Null.$TypeDescriptor$);
    }
    
    @Override
    public boolean contains(Object instance) {
        return instanceToId.containsKey(instance);
    }
    
    /** Get the id of the given registered instance */
    <Instance> Object getId(Instance instance) {
        Object object = instanceToId.get(instance);
        if (object == null) {
            throw new ceylon.language.AssertionError("Instance " + instance + " has not been registered for serialization");
        }
        return object;
    }
    
    /**
     * "Create a reference to the given [[instance]] of 
     [[Class]], assigning it the given [[identifer|id]]."
     @throws Exception "if there is already an instance with the given
         identifier"
     */
    @Override
    public <Instance> SerializableReference<Instance> reference(TypeDescriptor reified$Instance, Object id, Instance instance) {
        Object otherInstance = instanceToId.put(instance, id);
        if (otherInstance != null
                && instance != null
                && otherInstance != instance) {
            throw new ceylon.language.AssertionError("A different instance has already been registered with id "+id+": \"" + otherInstance +"\", \""+ instance+"\"");
        }
        SerializableReferenceImpl ref = new SerializableReferenceImpl(reified$Instance, this, id, instance);
        SerializableReference<Object> prevReference = idToReference.put(id, ref);
        if (prevReference != null) {
            throw new ceylon.language.AssertionError("A different instance has already been registered with id "+id+": \"" + prevReference.instance() +"\", \""+ instance+"\"");
        }
        return ref;
    }

    @Override
    public Iterator<? extends SerializableReference<Object>> iterator() {
        return new Iterator<SerializableReference<Object>>() {
            private final java.util.Iterator<SerializableReference<Object>> iter = idToReference.values().iterator();
            @Override
            public Object next() {
                if (!iter.hasNext()) {
                    return finished_.get_();
                }
                return iter.next();
            }
            
        };
    }
    
    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(SerializationContextImpl.class);
    }
}

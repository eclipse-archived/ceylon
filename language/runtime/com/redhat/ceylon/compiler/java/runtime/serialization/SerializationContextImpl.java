package com.redhat.ceylon.compiler.java.runtime.serialization;

import java.util.IdentityHashMap;

import ceylon.language.Iterator;
import ceylon.language.Null;
import ceylon.language.finished_;
import ceylon.language.impl.BaseIterable;
import ceylon.language.serialization.SerializationContext;
import ceylon.language.serialization.StatefulReference;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

/**
 * The implementation of {@link SerializationContext}
 */
@Ceylon(major=7, minor=0)
@Class
@SatisfiedTypes("ceylon.language.serialization::SerializationContext")
public class SerializationContextImpl 
        extends BaseIterable<StatefulReference<Object>, Object> 
        implements SerializationContext, ReifiedType {
    
    private final IdentityHashMap<Object, Object> instanceToId = new IdentityHashMap<>();
    
    public SerializationContextImpl() {
        super(TypeDescriptor.klass(SerializingStatefulReference.class, ceylon.language.Object.$TypeDescriptor$), Null.$TypeDescriptor$);
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
    public <Instance> StatefulReference<Instance> reference(TypeDescriptor reified$Instance, Object id, Instance instance) {
        Object otherInstance = instanceToId.put(instance, id);
        if (otherInstance != null
                && instance != null
                && otherInstance != instance) {
            throw new ceylon.language.AssertionError("A different instance has already been registered with id "+id+": \"" + otherInstance +"\", \""+ instance+"\"");
        }
        return new SerializingStatefulReference(reified$Instance, this, id, instance);
    }

    @Override
    public Iterator<? extends StatefulReference<Object>> iterator() {
        return new Iterator<StatefulReference<Object>>() {
            private final java.util.Iterator<Object> iter = instanceToId.keySet().iterator();
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

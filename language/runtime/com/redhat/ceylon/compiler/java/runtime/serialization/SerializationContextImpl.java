package com.redhat.ceylon.compiler.java.runtime.serialization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import ceylon.language.Iterator;
import ceylon.language.Null;
import ceylon.language.finished_;
import ceylon.language.impl.BaseIterable;
import ceylon.language.meta.type_;
import ceylon.language.meta.model.ClassModel;
import ceylon.language.serialization.Deconstructor;
import ceylon.language.serialization.SerializationContext;
import ceylon.language.serialization.SerializableReference;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
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
    
    private final ArrayList<SerializableReference<?>> references = new ArrayList<>();
    private final IdentityHashMap<Object, SerializableReference<?>> identifiableToReference = new IdentityHashMap<>();
    private final HashMap<Object, SerializableReference<?>> unidentifiableToReference = new HashMap<>();
    
    private Map<Object, SerializableReference<?>> map(Object instance) {
        if (Util.isIdentifiable(instance)) {
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
        SerializableReferenceImpl<Instance> ref;
        if (type_.type(reified$Instance, instance).getDeclaration().getAnonymous()) {
            ref = new SerializableReferenceImpl<Instance>(reified$Instance, this, id, instance) {
                @Override
                public Object serialize(final Deconstructor dtor) {
                    return null;
                }
            };
        } else {
            ref = new SerializableReferenceImpl<Instance>(reified$Instance, this, id, instance);
        }
        SerializableReference<?> prevReference = map(instance).put(instance, ref);
        if (prevReference != null) {
            throw new ceylon.language.AssertionError("The instance \""+prevReference.instance()+"\" has already been registered with id "+id+": You cannot register \"" + instance + "\" again with a different id");
        }
        references.add(ref);
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
            private int index = 0;
            @Override
            public Object next() {
                if (index < references.size()) {
                    return references.get(index++);
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

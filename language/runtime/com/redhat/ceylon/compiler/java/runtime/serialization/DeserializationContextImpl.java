package com.redhat.ceylon.compiler.java.runtime.serialization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ceylon.language.AssertionError;
import ceylon.language.Iterator;
import ceylon.language.Null;
import ceylon.language.finished_;
import ceylon.language.impl.BaseIterable;
import ceylon.language.meta.declaration.ValueDeclaration;
import ceylon.language.serialization.Deconstructed;
import ceylon.language.serialization.DeserializationContext;
import ceylon.language.serialization.RealizableReference;
import ceylon.language.serialization.Reference;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

/**
 * The implementation of {@link DeserializationContext}
 */
@Ceylon(major = 8)
@Class
@SatisfiedTypes("ceylon.language.serialization::DeserializationContext")
public class DeserializationContextImpl 
    extends BaseIterable<Reference<? extends Object>, Object> 
    implements DeserializationContext, ReifiedType {

    // TODO Shouldn't this be keyed on (id, class), and in fact shouldn't 
    // the classed-ness of ids be a concern of the serialization library?
    private final Map<Object, DeserializingReference<?>> idToReference = new HashMap<>();
    private final ArrayList<Reference<?>> references = new ArrayList<>();
    
    public DeserializationContextImpl() {
        super(ceylon.language.Object.$TypeDescriptor$, Null.$TypeDescriptor$);
    }
    
    @Override
    public <Instance> Reference<Instance> reference(
            TypeDescriptor reified$Instance, 
            Object id,
            @SuppressWarnings("rawtypes") ceylon.language.meta.model.Class classModel) {
        DeserializingReference<?> ref = idToReference.get(id);
        if (ref != null) {
            return (Reference)ref;
        }
        if (classModel.getDeclaration().getAbstract()) {
            throw new AssertionError("class is abstract: " + classModel);
        }
        ValueDeclaration anon = classModel.getDeclaration().getObjectValue();
        if (anon != null) {
            ref = new DeserializingReference<Instance>(reified$Instance, id, classModel, DeserializingReference.ST_INITIALIZED, (Instance)anon.get()) {
                @Override
                public RealizableReference<Instance> deserialize(Deconstructed deconstructed) {
                    return this;
                }
            };
        } else {
            ref = new DeserializingReference<Instance>(reified$Instance, this, id, classModel, (Reference)null);
        }
        idToReference.put(id, ref);
        references.add(ref);
        return (Reference)ref;
    }
    
    @Override
    public <Instance> Reference<Instance> getReference(TypeDescriptor reified$Instance, 
            Object id) {
        DeserializingReference<?> ref = idToReference.get(id);
        return (Reference)ref;
    }
    
    @Override
    public <Outer, Instance> Reference<? extends Instance> memberReference(
            TypeDescriptor reified$Outer,
            TypeDescriptor reified$Instance, 
            Object id,
            @SuppressWarnings("rawtypes") ceylon.language.meta.model.MemberClass classModel,
            Reference<? extends Outer> outerReference) {
        DeserializingReference<?> ref = idToReference.get(id);
        if (ref != null) {
            if (ref.getClazz().equals(classModel)) {
                return (Reference)ref;
            } else {
                throw new AssertionError("reference already made to instance with a different class");
            }
        }
        if (classModel.getDeclaration().getAbstract()) {
            throw new AssertionError("class is abstract: " + classModel);
        }
        ref = new DeserializingReference<Instance>(reified$Instance, this, id, classModel, outerReference);
        idToReference.put(id, ref);
        references.add((Reference)ref);
        return (Reference)ref;
    }

    boolean containsId(Object id) {
        return idToReference.containsKey(id);
    }
    
    /**
     * Returns the reference to the instance with the given id. 
     * Never returns null.
     */
    Object leakReferred(Object id) {
        DeserializingReference<?> reference = idToReference.get(id);
        if (reference == null) {
            throw new AssertionError("cannot obtain reference to unregistered id: " + id);
        }
        return (($InstanceLeaker$)reference).$leakInstance$();
    }

    @Override
    public Iterator<? extends Reference<Object>> iterator() {
        return new Iterator<Reference<Object>>() {
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
        return TypeDescriptor.klass(DeserializationContextImpl.class);
    }

}
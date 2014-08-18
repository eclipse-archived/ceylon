package com.redhat.ceylon.compiler.java.runtime.serialization;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import ceylon.language.AssertionError;
import ceylon.language.Finished;
import ceylon.language.Tuple;
import ceylon.language.meta.type_;
import ceylon.language.meta.model.ClassModel;
import ceylon.language.serialization.Deconstructed;
import ceylon.language.serialization.Deconstructor;
import ceylon.language.serialization.Reference;
import ceylon.language.serialization.StatefulReference;
import ceylon.language.serialization.StatelessReference;

import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

class SerializingStatefulReference<Instance> 
        implements StatefulReference<Instance>, ReifiedType {
    private final TypeDescriptor reified$Instance;
    private final Object id;
    private final Instance instance;
    
    SerializingStatefulReference(TypeDescriptor reified$Instance, SerializationContextImpl context, Object id, Instance instance) {
        this.reified$Instance = reified$Instance;
        this.id = id;
        this.instance = instance;
    }
    
    public String toString() {
        return id +"=>" + instance;
    }
    
    @Override
    public /*Deconstructed<Instance>*/ Object serialize(Deconstructor deconstructor) {
        ((Serializable)this.instance).$serialize$(deconstructor);
        return null;
    }

    @Override
    public Instance instance() {
        return instance;
    }
    
    /** 
     * Construct the instance (by calling its constructor) and set 
     * its value-typed attributes. 
     * Does not set the reference-typed attributes
     */
    @Override
    public Object reconstruct() {
        // XXX no op because we're for serialization and this only makes sense for deserialization?
        return null;
    }
    

    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(SerializingStatefulReference.class, reified$Instance);
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


class DeserializingStatefulReference<Instance> 
        implements StatefulReference<Instance>, $InstanceLeaker$<Instance>, ReifiedType {
    
    private final TypeDescriptor reified$Instance;
    private final DeserializationContextImpl context;
    private final Object id;
    private final Instance instance;
    private Deconstructed deconstructed;
    static enum State {
        UNINITIALIZED,
        UNINITIALIZED_REFS,
        INITIALIZED
    }
    private State state;
    
    /**
     * Create a stateful reference to the instance with 
     * the given {@code id} and {@code clazz}.
     * 
     * An instance of {@code clazz} is instantiated and associated with the 
     * {@code context}. The {@code deconstructed} is kept for use later by 
     * {@link #reconstruct()}.
     *  
     * @param reified$Instance
     * @param context
     * @param id
     * @param clazz
     * @param deconstructed
     */
    DeserializingStatefulReference(TypeDescriptor reified$Instance, 
            DeserializationContextImpl context, Object id, 
            Instance instance, 
            Deconstructed deconstructed) {
        this.reified$Instance = reified$Instance;
        this.context = context;
        this.id = id;
        this.instance = instance;
        this.deconstructed = deconstructed;
        this.state = State.UNINITIALIZED;
    }
    
    public String toString() {
        switch (state) {
        case UNINITIALIZED:
            return "unitialized " + id +"<=((" + deconstructed + "))";
        case UNINITIALIZED_REFS:
            return "unitialized refs " + id +"<=(" + deconstructed + ")";
        case INITIALIZED:
            return id +"<=" + instance;
        }
        throw new AssertionError("Illegal state");
    }
    
    
    @Override
    public /*Deconstructed<Instance>*/ Object serialize(Deconstructor deconstructor) {
        // TODO What does this mean in the context of derserialization?
        throw new AssertionError("WTF?");
    }
    
    @Override
    public Instance instance() {
        // !!!!! XXX MUST NOT LEAK PARTIALLY BUILT OBJECTS
        // XXX HERE we must ensure that this.instance has been 
        // reconstructed, but also that everything it references 
        // (transitively) has been reconstructed too.
        // We can do this by inspecting this.deconstructed finding the 
        // references (from this.context) and ensuring those are 
        // reconstructed.
        reconstruct();
        return instance;
    }
    
    /**
     * Reconstructs this instance
     */
    @Override
    public Object reconstruct() {
        if (state != State.INITIALIZED) {
            LinkedList<DeserializingStatefulReference<?>> queue = new LinkedList<DeserializingStatefulReference<?>>();
            queue.addLast(this);
            while (!queue.isEmpty()) {
                DeserializingStatefulReference<?> r = queue.removeFirst();
                if (r.state == State.UNINITIALIZED) {
                    ((Serializable)r.instance).$deserialize$(r.deconstructed);
                    r.state = State.UNINITIALIZED_REFS;
                }
                if (r.state == State.UNINITIALIZED_REFS) {
                    for (Reference<Object> referred : r.references()) {
                        if (referred instanceof StatelessReference) {
                            throw new AssertionError("reference " + referred.getId() + " has not been deserialized");
                        }
                        DeserializingStatefulReference<?> statefulReferred = (DeserializingStatefulReference<?>)referred;
                        if (statefulReferred.state != State.INITIALIZED) {
                            queue.addLast(statefulReferred);
                        }
                    }
                    // This is actually too weak: If an exception is thrown
                    // while initialising some other thing (already in, or 
                    // yet to be added to the queue), then it will be possible 
                    // to obtain a reference to a broken thing
                    // We could track this on a per-instance basis 
                    // (but that means tracking the reverse dependencies, transitively)
                    // Or on a per-context basis, so that instance always 
                    // throws if there was ever an exception, even if the 
                    // broken object is not each reachable from the 
                    // instance being sought
                    
                    r.state = State.INITIALIZED;
                    r.deconstructed = null;
                }
            }
        }
        return null;
    }
    
    /**
     * The references in the Deconstructed
     * @return
     */
    private Iterable<Reference<Object>> references() {
        return new Iterable<Reference<Object>>() {

            @Override
            public java.util.Iterator<Reference<Object>> iterator() {
                return new java.util.Iterator<Reference<Object>>() {
                    ceylon.language.Iterator it = deconstructed.iterator();
                    Object next = null;
                    
                    @Override
                    public boolean hasNext() {
                        if (next == null) {
                            Object vdValue = it.next();
                            while (true) {
                                if (vdValue instanceof Finished) {
                                    next = vdValue;
                                    break;
                                }
                                Object valueOrRef = ((Tuple)vdValue).getFromFirst(1);
                                if (valueOrRef instanceof Reference) {
                                    next = valueOrRef;
                                    break;
                                }
                                vdValue = it.next();
                            }
                        }
                        return !(next instanceof Finished);
                    }
                    @Override
                    public Reference<Object> next() {
                        if (next == null) {
                            hasNext();
                        }
                        if (next instanceof Finished) {
                            throw new NoSuchElementException();
                        }
                        Reference<Object> result = (Reference)next;
                        next = null;
                        return result;
                    }
                    
                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                    
                };
            }
            
        };
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
    
    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(DeserializingStatefulReference.class, reified$Instance);
    }

    @Override
    public Instance $leakInstance$() {
        return instance;
    }
}
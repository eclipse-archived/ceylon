package com.redhat.ceylon.compiler.java.runtime.serialization;

public interface $InstanceLeaker$<Instance> {
    /**
     * The <strong>possibly uninitialized</strong> instance held by this
     * Reference. Only to be used in the compiler generated 
     * implementation of
     * {@link Serializable#$deserialize$(ceylon.language.serialization.Deconstructed)} 
     * so that the object graph can be reconstructed in the presence 
     * of cycles.
     */
    public Instance $leakInstance$();
}

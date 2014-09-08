package com.redhat.ceylon.compiler.java.runtime.serialization;

/**
 * <p>We need to be able to instantiate serializable classes bypassing their 
 * normal constructor. To do this the compiler creates a constructor
 * with a unique signature (and one which is not denoteable in Ceylon):
 * {@code <init>($Serialization$), ...} 
 * (where ... means some number of {code TypeDescriptor}s)</p>
 *  
 * <p>These constructors are always invoked with a null first argument.</p>
 * 
 * @see DeserializableReferenceImpl#DeserializableReferenceImpl(com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor, DeserializationContextImpl, Object, ceylon.language.meta.model.ClassModel)
 */
public final class $Serialization$ {
    private $Serialization$() {}
}

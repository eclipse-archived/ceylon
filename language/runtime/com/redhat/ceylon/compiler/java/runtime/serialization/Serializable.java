package com.redhat.ceylon.compiler.java.runtime.serialization;

import java.lang.invoke.MethodHandle;
import java.util.Collection;

import ceylon.language.serialization.Deconstructor;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

/**
 * Interface implicitly implemented by {@code serializable} classes. 
 * The methods in this interface are generated 
 * automatically by the compiler.
 * 
 * @see $Serialization$
 */
@Ignore
public interface Serializable {
    public void $serialize$(Deconstructor deconstructor);
    /** 
     * Return a collection of objects identifying the assignable state of 
     * the class of this instance.
     * 
     * For Arrays this returns a {@code Collection<java.lang.Integer>} 
     * (the indexes)
     * for everything else this returns a {@code Collection<java.lang.String>}
     * (the Ceylon attribute names).
     * 
     * This is static data, but since the caller doesn't know the type 
     * as compile time, it is easiest to make this a virtual method.
     * */
    public Collection<java.lang.Object> $references$();
    /**
     * Set the given attribute or element to the given value.
     * @param attributeOrIndex
     * @param ref
     */
    public void $set$(java.lang.Object attributeOrIndex, java.lang.Object ref);
}

package com.redhat.ceylon.compiler.java.runtime.serialization;

import ceylon.language.serialization.Deconstructed;
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
    public void $deserialize$(Deconstructed deconstructed);
}

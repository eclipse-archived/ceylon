package com.redhat.ceylon.compiler.java.runtime.serialization;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

import ceylon.language.Callable;
import ceylon.language.serialization.Deconstructor;
import ceylon.language.serialization.Deconstructed;

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

package com.redhat.ceylon.compiler.java.runtime.serialization;

import ceylon.language.Callable;
import ceylon.language.serialization.Deconstructor;
import ceylon.language.serialization.Deconstructed;

/**
 * Interface implicitly implemented by serializable classes. 
 * The methods in this inerface are generated 
 * automatically by the compiler.
 */
public interface Serializable {
    public void $serialize$(Callable<? extends Deconstructor> deconstructor);
    public void $deserialize$(Deconstructed deconstructed);
}

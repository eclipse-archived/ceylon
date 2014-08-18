package com.redhat.ceylon.compiler.java.runtime.serialization;

import ceylon.language.serialization.Deconstructor;
import ceylon.language.serialization.Deconstructed;

/**
 * Interface implicitly implemented by serializable classes
 */
public interface Serializable {
    public void $serialize$(Deconstructor deconstructor);
    public void $deserialize$(Deconstructed deconstructed);
}

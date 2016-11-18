package com.redhat.ceylon.compiler.java.wrapping;

import java.io.Serializable;

/**
 * A conversion from instances of one type to instances of another.
 */
public interface Wrapping<From, To> 
        extends Serializable {
    /** Convert the given element */
    To wrap(From from);
    /** A Wrapping that applies the inverse conversion, or null if no such Wrapping exists. */
    Wrapping<To,From> inverted();
}
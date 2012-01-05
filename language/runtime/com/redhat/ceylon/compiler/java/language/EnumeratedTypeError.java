package com.redhat.ceylon.compiler.java.language;

/**
 * <p>Thrown when an unexpected subclass of a enumerated type is encountered, 
 * for example in an exhaustive {@code switch} statement.</p>
 * 
 * <p>The subtypes of enumerated types are normally verified at compile time. 
 * This error can only occur if the compiler's checks are subverted, for 
 * example by subclassing an enumerated type in Java code, or by adding a 
 * subclass without recompiling dependent code.</p>
 *  
 * @author tom
 */
public class EnumeratedTypeError extends Error {

    public EnumeratedTypeError(String message) {
        super(message);
    }

}

package com.redhat.ceylon.compiler.java.language;

/**
 * Indicates the occurance of an 'impossible' situation which breaks Ceylon's
 * type system. For instance, a binary incompatibility arising from a 
 * subclass being added to an enumerated type.
 * @author tom
 */
public class TypeSystemError extends Error {

    public TypeSystemError(String message) {
        super(message);
    }

}

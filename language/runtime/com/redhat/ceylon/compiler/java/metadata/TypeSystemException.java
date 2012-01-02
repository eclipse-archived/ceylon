package com.redhat.ceylon.compiler.java.metadata;

/**
 * Indicates the occurance of an 'impossible' situation which breaks Ceylon's
 * type system. For instance, a binary incompatibility arising from a 
 * subclass being added to an enumerated type.
 * @author tom
 */
public class TypeSystemException extends RuntimeException {

    public TypeSystemException(String message) {
        super(message);
    }

}

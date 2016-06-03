package com.redhat.ceylon.compiler.java.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;

/**
 * Thrown when an operation which requires reified type information is 
 * evaluated with an instance or type which lacks the necessary information
 * @author tom
 */
@Ceylon(major = 8)
@Class
public class ReifiedTypeError extends Error {
    
    private static final long serialVersionUID = -2854641361615943896L;

    public ReifiedTypeError(String message) {
        super(message);
    }
}

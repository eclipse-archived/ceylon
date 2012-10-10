package com.redhat.ceylon.compiler.java.tools;

/**
 * This exeption is meant to cleanly show error messages to the user
 * without distracting exception names or stack traces
 */
public class CompilerErrorException extends RuntimeException {

    public CompilerErrorException() {
    }

    public CompilerErrorException(String message) {
        super(message);
    }

    public CompilerErrorException(Throwable cause) {
        super(cause);
    }

    public CompilerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompilerErrorException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

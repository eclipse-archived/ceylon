package com.redhat.ceylon.compiler;


/**
 * Thrown when the compiler exits abnormally.
 */
public class CompilerBugException extends RuntimeException {
    CompilerBugException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

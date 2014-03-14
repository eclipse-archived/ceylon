package com.redhat.ceylon.compiler.java.language;

/**
 * Error used by the compiler when an expression or statement 
 * (that's not a declaration) has typechecker errors.
 */
public class UnresolvedCompilationError extends Error {

    private static final long serialVersionUID = -6034493434470699238L;

    public UnresolvedCompilationError() {
        super();
    }

    public UnresolvedCompilationError(String message, Throwable cause) {
        super(message, cause);
    }

    public UnresolvedCompilationError(String message) {
        super(message);
    }

    public UnresolvedCompilationError(Throwable cause) {
        super(cause);
    }

}

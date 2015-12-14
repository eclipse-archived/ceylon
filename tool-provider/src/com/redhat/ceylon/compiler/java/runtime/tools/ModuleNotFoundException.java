package com.redhat.ceylon.compiler.java.runtime.tools;

public class ModuleNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ModuleNotFoundException() {
        super();
    }

    public ModuleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModuleNotFoundException(String message) {
        super(message);
    }

    public ModuleNotFoundException(Throwable cause) {
        super(cause);
    }

}

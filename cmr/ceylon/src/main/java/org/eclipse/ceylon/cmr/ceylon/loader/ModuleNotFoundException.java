package org.eclipse.ceylon.cmr.ceylon.loader;

public class ModuleNotFoundException extends Exception {

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

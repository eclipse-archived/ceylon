package com.redhat.ceylon.model.loader;

public class ModelResolutionException extends RuntimeException {

    public ModelResolutionException() {
        super();
    }

    public ModelResolutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelResolutionException(String message) {
        super(message);
    }

    public ModelResolutionException(Throwable cause) {
        super(cause);
    }

}

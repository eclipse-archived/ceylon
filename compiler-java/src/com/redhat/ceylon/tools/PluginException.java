package com.redhat.ceylon.tools;

public class PluginException extends RuntimeException {

    PluginException() {
        super();
    }

    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginException(String message) {
        super(message);
    }

    public PluginException(Throwable cause) {
        super(cause);
    }

}

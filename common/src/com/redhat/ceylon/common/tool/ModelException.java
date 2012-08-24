package com.redhat.ceylon.common.tool;

public class ModelException extends PluginException {

    public ModelException(String message) {
        super(message);
    }
    
    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }

}

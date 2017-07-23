package com.redhat.ceylon.common.tool;

public class ModelException extends ToolException {

    private static final long serialVersionUID = -5680564442795816474L;

    public ModelException(String message) {
        super(message);
    }
    
    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }

}

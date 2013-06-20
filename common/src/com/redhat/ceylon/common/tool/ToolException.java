package com.redhat.ceylon.common.tool;

/**
 * An exception in the tool API.
 */
public class ToolException extends RuntimeException {

    protected ToolException() {
        super();
    }

    protected ToolException(String message, Throwable cause) {
        super(message, cause);
    }

    protected ToolException(String message) {
        super(message);
    }

    public ToolException(Throwable cause) {
        super(cause);
    }

}

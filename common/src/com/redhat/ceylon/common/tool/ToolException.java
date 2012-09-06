package com.redhat.ceylon.common.tool;

public class ToolException extends RuntimeException {

    ToolException() {
        super();
    }

    public ToolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToolException(String message) {
        super(message);
    }

    public ToolException(Throwable cause) {
        super(cause);
    }

}

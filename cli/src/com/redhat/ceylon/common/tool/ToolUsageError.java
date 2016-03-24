package com.redhat.ceylon.common.tool;

public class ToolUsageError extends ToolError {

    public ToolUsageError(String message, Throwable cause) {
        super(message, cause);
    }

    public ToolUsageError(String message) {
        super(message);
    }

    public ToolUsageError(Throwable cause) {
        super(cause);
    }

}

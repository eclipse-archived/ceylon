package com.redhat.ceylon.common.tool;

public class ToolUsageError extends ToolError {

    private static final long serialVersionUID = -6867053402431389324L;

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

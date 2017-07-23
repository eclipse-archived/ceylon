package com.redhat.ceylon.common.tool;

/**
 * A {@link ToolError} that should be considered fatal (i.e. a bug in the tool)
 */
public abstract class FatalToolError extends ToolError {

    private static final long serialVersionUID = 2151486863112265165L;

    public FatalToolError(String message, Throwable cause) {
        super(message, cause);
    }

    public FatalToolError(String message) {
        super(message);
    }

    public FatalToolError(Throwable cause) {
        super(cause);
    }

    public boolean getShowStacktrace() {
        return true;
    }
}

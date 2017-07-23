package com.redhat.ceylon.common.tool;

public class NonFatalToolMessage extends ToolError {

    private static final long serialVersionUID = 8749025375355286279L;

    public NonFatalToolMessage(String message) {
        super(message, 0);
    }
}

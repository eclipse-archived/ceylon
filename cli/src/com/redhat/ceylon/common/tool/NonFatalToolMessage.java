package com.redhat.ceylon.common.tool;

public class NonFatalToolMessage extends ToolError {

    public NonFatalToolMessage(String message) {
        super(message, 0);
    }
}

package com.redhat.ceylon.compiler.js;

import com.redhat.ceylon.common.tool.ToolError;

public class CompilerErrorException extends ToolError {

    private static final long serialVersionUID = 5L;

    public CompilerErrorException(String message) {
        super(message);
    }
}

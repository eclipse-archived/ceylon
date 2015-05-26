package com.redhat.ceylon.compiler.js;

import com.redhat.ceylon.common.tool.ToolError;


public class CeylonRunJsException extends ToolError {

    private static final long serialVersionUID = 1L;

    public CeylonRunJsException(String message) {
        super(message);
    }
}

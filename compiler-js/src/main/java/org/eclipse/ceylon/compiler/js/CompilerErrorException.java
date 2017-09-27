package org.eclipse.ceylon.compiler.js;

import org.eclipse.ceylon.common.tool.ToolError;

public class CompilerErrorException extends ToolError {

    private static final long serialVersionUID = 5L;

    public CompilerErrorException(String message) {
        super(message);
    }
}

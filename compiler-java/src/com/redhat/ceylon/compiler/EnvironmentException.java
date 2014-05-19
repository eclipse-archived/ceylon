package com.redhat.ceylon.compiler;

import com.redhat.ceylon.common.tool.ToolError;

/**
 * Something unexpected in the environment prevented the compilation
 * from succeeding. This is a fatal error but not a bug. 
 */
public class EnvironmentException extends ToolError {

    public EnvironmentException(Throwable cause) {
        super(cause);
    }
}

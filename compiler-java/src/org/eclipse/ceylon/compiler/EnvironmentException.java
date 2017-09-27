package org.eclipse.ceylon.compiler;

import org.eclipse.ceylon.common.tool.ToolError;

/**
 * Something unexpected in the environment prevented the compilation
 * from succeeding. This is a fatal error but not a bug. 
 */
@SuppressWarnings("serial")
public class EnvironmentException extends ToolError {

    public EnvironmentException(Throwable cause) {
        super(cause);
    }
}

package com.redhat.ceylon.compiler;

import com.redhat.ceylon.common.tool.FatalToolError;

/**
 * Exception type to indicate a system error with the compiler.
 * System errors are often (but not always) due to bugs in the compiler.
 */
public class SystemErrorException extends FatalToolError {

    SystemErrorException(Throwable cause) {
        super(cause);
    }
    
    public String getErrorMessage() {
        return CeylonCompileMessages.msgSystemError();
    }
}

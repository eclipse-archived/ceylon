package org.eclipse.ceylon.compiler.typechecker.tree;

import org.eclipse.ceylon.common.Backend;

public interface Message {
    String getMessage();
    int getCode();
    /**
     * Returns the associated ErrorCode, or null if the code does not have an
     * associated ErrorCode instance, in which case, use getCode().
     */
    ErrorCode getErrorCode();
    int getLine();
    Backend getBackend();
    boolean isWarning();
}

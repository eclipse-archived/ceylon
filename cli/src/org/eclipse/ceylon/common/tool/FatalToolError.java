/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tool;

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

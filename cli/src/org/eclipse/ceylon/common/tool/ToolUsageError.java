/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tool;

public class ToolUsageError extends ToolError {

    private static final long serialVersionUID = -6867053402431389324L;

    public ToolUsageError(String message, Throwable cause) {
        super(message, cause);
    }

    public ToolUsageError(String message) {
        super(message);
    }

    public ToolUsageError(Throwable cause) {
        super(cause);
    }

}

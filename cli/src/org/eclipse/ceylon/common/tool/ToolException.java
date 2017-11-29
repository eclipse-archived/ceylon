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

/**
 * An exception in the tool API.
 */
public class ToolException extends RuntimeException {

    private static final long serialVersionUID = 717634710054953267L;

    ToolException() {
        super();
    }

    ToolException(String message, Throwable cause) {
        super(message, cause);
    }

    ToolException(String message) {
        super(message);
    }

    public ToolException(Throwable cause) {
        super(cause);
    }

}

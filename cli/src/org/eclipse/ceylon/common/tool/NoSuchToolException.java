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

public class NoSuchToolException extends OptionArgumentException.InvalidArgumentValueException {

    private static final long serialVersionUID = 4847884252620666903L;
    
    private String toolName;

    public NoSuchToolException(ArgumentModel<?> argumentModel, String toolName) {
        super(null, argumentModel, toolName);
        this.toolName = toolName;
    }
    
    public String getToolName() {
        return toolName;
    }

}

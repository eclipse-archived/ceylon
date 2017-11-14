/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.tools.new_;

import java.io.File;

import org.eclipse.ceylon.common.tool.Argument;

public abstract class Project extends NewSubTool {

    @Argument(argumentName="dir", multiplicity="?", order=1)
    public void setDirectory(File directory) {
        super.setDirectory(directory);
    }
    
}

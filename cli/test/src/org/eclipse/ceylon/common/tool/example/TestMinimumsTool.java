/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tool.example;

import java.util.List;

import org.eclipse.ceylon.common.tool.Argument;
import org.eclipse.ceylon.common.tool.Summary;
import org.eclipse.ceylon.common.tool.Tool;
import org.eclipse.ceylon.common.tools.CeylonTool;

@Summary("")
public class TestMinimumsTool implements Tool {

    private List<String> arguments;
    
    public List<String> getArguments() {
        return arguments;
    }

    @Argument(multiplicity="[3,]")
    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public void initialize(CeylonTool mainTool) {
    }

    @Override
    public void run() {
    }

}

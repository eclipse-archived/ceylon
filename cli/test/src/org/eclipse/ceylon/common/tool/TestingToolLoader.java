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

import org.eclipse.ceylon.common.tool.ServiceToolLoader;
import org.eclipse.ceylon.common.tool.example.Workaround;

public class TestingToolLoader extends ServiceToolLoader {

    public TestingToolLoader() {
        super(Workaround.class);
    }

    @Override
    public String getToolName(String className) {
        return camelCaseToDashes(className.replaceAll("^(.*\\.)?Test(.*)Tool$", "$2"));
    }

}

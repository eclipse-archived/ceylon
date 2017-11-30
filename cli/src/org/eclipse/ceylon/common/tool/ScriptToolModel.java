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

public class ScriptToolModel<T extends Tool> extends ToolModel<T> {
    private final String scriptName;

    public ScriptToolModel(String name, String scriptName) {
        super(name);
        this.scriptName = scriptName;
    }

    public String getScriptName() {
        return scriptName;
    }
    
    @Override
    public boolean isPlumbing() {
        return false;
    }
}

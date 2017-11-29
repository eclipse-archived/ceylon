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
 * A command line option or option argument accepted by a plugin
 */
public class OptionModel<A> {
    public static enum ArgumentType {
        BOOLEAN,
        OPTIONAL,
        REQUIRED
    }
    private ToolModel<?> toolModel;
    private String longName;
    private Character shortName;
    private ArgumentModel<A> argument;
    private ArgumentType argumentType;
    
    public ToolModel<?> getToolModel() {
        return toolModel;
    }
    public void setToolModel(ToolModel<?> toolModel) {
        this.toolModel = toolModel;
    }
    public String getLongName() {
        return longName;
    }
    public void setLongName(String name) {
        this.longName = name;
    }
    public Character getShortName() {
        return shortName;
    }
    public void setShortName(Character shortName) {
        this.shortName = shortName;
    }
    public ArgumentModel<A> getArgument() {
        return argument;
    }
    public void setArgument(ArgumentModel<A> argument) {
        this.argument = argument;
    }
    public void setArgumentType(ArgumentType argumentType) {
        this.argumentType = argumentType;
    }
    public ArgumentType getArgumentType() {
        return this.argumentType;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (longName != null) {
            sb.append("--").append(longName);
        }
        if (longName != null && shortName != null) {
            sb.append("/");
        }
        if (shortName != null) {
            sb.append("-").append(shortName);
        }
        return sb.toString();
    }
}

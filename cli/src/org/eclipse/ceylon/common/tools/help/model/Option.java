/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tools.help.model;

import org.eclipse.ceylon.common.tool.OptionModel;
import org.eclipse.ceylon.common.tool.OptionModel.ArgumentType;
import org.tautua.markdownpapers.ast.Node;

public class Option implements Documentation {

    private OptionModel<?> option;
    
    private Node description;

    public OptionModel<?> getOption() {
        return option;
    }

    public void setOption(OptionModel<?> option) {
        this.option = option;
    }

    public Node getDescription() {
        return description;
    }

    public void setDescription(Node description) {
        this.description = description;
    }

    public String getLongName() {
        String longName = option.getLongName() != null ? "--" + option.getLongName() : null;
        return longName;
    }

    public String getShortName() {
        String shortName = option.getShortName() != null ? "-" + option.getShortName() : null;
        return shortName;
    }

    public String getArgumentName() {
        String argumentName = option.getArgumentType() == ArgumentType.BOOLEAN ? null : option.getArgument().getName();
        return argumentName;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitOption(this);
    }
    
}

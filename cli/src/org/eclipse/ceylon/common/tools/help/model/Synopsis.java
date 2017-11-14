/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tools.help.model;

import java.util.Collections;
import java.util.List;

import org.eclipse.ceylon.common.tool.ArgumentModel;
import org.eclipse.ceylon.common.tool.OptionModel;


public class Synopsis {

    private String invocation;
    
    private List<?> optionsAndArguments = Collections.emptyList();
    
    public List<?> getOptionsAndArguments() {
        return optionsAndArguments;
    }

    public void setOptionsAndArguments(List<?> options) {
        this.optionsAndArguments = options;
    }

    public String getInvocation() {
        return invocation;
    }

    public void setInvocation(String invocation) {
        this.invocation = invocation;
    }

    public void accept(Visitor visitor) {
        visitor.startSynopsis(this);
        for (Object o : getOptionsAndArguments()) {
            if (o instanceof OptionModel<?>) {
                visitor.visitSynopsisOption((OptionModel<?>)o);
            } else if (o instanceof SubtoolVisitor.ToolModelAndSubtoolModel) {
                visitor.visitSynopsisSubtool((SubtoolVisitor.ToolModelAndSubtoolModel)o);
            } else if (o instanceof ArgumentModel<?>) {
                visitor.visitSynopsisArgument((ArgumentModel<?>)o);
            } 
        }
        visitor.endSynopsis(this);
    }

}

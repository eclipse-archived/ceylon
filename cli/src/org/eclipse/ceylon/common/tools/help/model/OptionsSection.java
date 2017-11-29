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

import java.util.Collections;
import java.util.List;

import org.tautua.markdownpapers.ast.Node;

public class OptionsSection implements Documentation {
    
    private Node title;
    
    private List<Option> options = Collections.emptyList();
    
    private List<OptionsSection> subsections = Collections.emptyList();

    public Node getTitle() {
        return title;
    }

    public void setTitle(Node title) {
        this.title = title;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
    
    public List<OptionsSection> getSubsections() {
        return subsections;
    }

    public void setSubsections(List<OptionsSection> subsections) {
        this.subsections = subsections;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.startOptions(this);
        for (Option option : options) {
            option.accept(visitor);
        }
        for (OptionsSection subsection : subsections) {
            subsection.accept(visitor);
        }
        visitor.endOptions(this);
    }

}

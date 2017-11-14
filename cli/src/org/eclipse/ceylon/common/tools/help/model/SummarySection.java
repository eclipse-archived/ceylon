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

import org.tautua.markdownpapers.ast.Node;

public class SummarySection implements Documentation {

    private Node title;
    
    private String summary;

    public Node getTitle() {
        return title;
    }

    public void setTitle(Node title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitSummary(this);
    }
}

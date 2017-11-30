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

public class SynopsesSection implements Documentation {
    
    private String title;
    
    private List<Synopsis> synopses = Collections.emptyList();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Synopsis> getSynopses() {
        return synopses;
    }

    public void setSynopses(List<Synopsis> synopses) {
        this.synopses = synopses;
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.startSynopses(this);
        for (Synopsis synopsis : synopses) {
            synopsis.accept(visitor);
        }
        visitor.endSynopses(this);
    }
}

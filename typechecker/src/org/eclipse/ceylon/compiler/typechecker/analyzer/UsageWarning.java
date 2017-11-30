/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.analyzer;

import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.compiler.typechecker.tree.AnalysisMessage;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;

/**
 * A warning to the user about a condition that is not 
 * serious enough to prevent generation of code.
 */
public class UsageWarning extends AnalysisMessage {
    
    private final String name;
    
    private boolean suppressed;
    
    public UsageWarning(Node treeNode, String message, String name) {
        this(treeNode, message, name, null);
    }
    
    public UsageWarning(Node treeNode, String message, String name, Backend backend) {
        super(treeNode, message, backend);
        this.name = name;
        this.suppressed = false;
    }
    
    @Override
    public boolean isWarning() {
        return true;
    }
    
    public String getWarningName() {
        return name;
    }
    
    public boolean isSuppressed() {
        return suppressed;
    }
    
    public void setSuppressed(boolean suppressed) {
        this.suppressed = suppressed;
    }
}

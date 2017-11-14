/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
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
import org.eclipse.ceylon.compiler.typechecker.tree.ErrorCode;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;

/**
 * Any error condition that should prevent the backends
 * from generating code. 
 */
public class AnalysisError extends AnalysisMessage {

    public AnalysisError(Node treeNode, String message) {
        super(treeNode, message);
    }
    
    public AnalysisError(Node treeNode, String message, Backend backend) {
        super(treeNode, message, backend);
    }
    
    public AnalysisError(Node treeNode, String message, int code) {
        super(treeNode, message, code);
    }
    
    public AnalysisError(Node treeNode, String message, int code, Backend backend) {
        super(treeNode, message, code, backend);
    }

    public AnalysisError(Node treeNode, String message, ErrorCode errorCode) {
        super(treeNode, message, errorCode);
    }
    
    public AnalysisError(Node treeNode, String message, ErrorCode errorCode, Backend backend) {
        super(treeNode, message, errorCode, backend);
    }

}

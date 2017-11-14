/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.tree;

import org.eclipse.ceylon.common.Backend;

/**
 * An unexpected error from the backend. Usually a bug
 * in the compiler. 
 */
public class UnexpectedError extends AnalysisMessage {

    public UnexpectedError(Node treeNode, String message) {
        super(treeNode, message);
    }
    
    public UnexpectedError(Node treeNode, String message, Backend backend) {
        super(treeNode, message, backend);
    }
    
}

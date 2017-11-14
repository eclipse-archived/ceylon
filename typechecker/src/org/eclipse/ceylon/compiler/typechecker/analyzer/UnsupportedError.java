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
import org.eclipse.ceylon.compiler.typechecker.tree.Node;

/**
 * Represents situations that the typechecker accepts,
 * because they are in principle well-typed, but that
 * the backends don't yet support. 
 */
public class UnsupportedError extends AnalysisError {
    
    public UnsupportedError(Node treeNode, String message) {
        super(treeNode, message);
    }
    
    public UnsupportedError(Node treeNode, String message, Backend backend) {
        super(treeNode, message, backend);
    }
    
}

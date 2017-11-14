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

public interface Message {
    String getMessage();
    int getCode();
    /**
     * Returns the associated ErrorCode, or null if the code does not have an
     * associated ErrorCode instance, in which case, use getCode().
     */
    ErrorCode getErrorCode();
    int getLine();
    Backend getBackend();
    boolean isWarning();
}

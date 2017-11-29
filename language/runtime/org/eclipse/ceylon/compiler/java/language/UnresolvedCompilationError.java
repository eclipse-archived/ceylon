/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.language;

/**
 * Error used by the compiler when an expression or statement 
 * (that's not a declaration) has typechecker errors.
 */
public class UnresolvedCompilationError extends Error {

    private static final long serialVersionUID = -6034493434470699238L;

    public UnresolvedCompilationError() {
        super();
    }

    public UnresolvedCompilationError(String message, Throwable cause) {
        super(message, cause);
    }

    public UnresolvedCompilationError(String message) {
        super(message);
    }

    public UnresolvedCompilationError(Throwable cause) {
        super(cause);
    }

}

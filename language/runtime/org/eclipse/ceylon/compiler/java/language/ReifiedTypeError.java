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

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Class;

/**
 * Thrown when an operation which requires reified type information is 
 * evaluated with an instance or type which lacks the necessary information
 * @author tom
 */
@Ceylon(major = 8)
@Class
public class ReifiedTypeError extends Error {
    
    private static final long serialVersionUID = -2854641361615943896L;

    public ReifiedTypeError(String message) {
        super(message);
    }
}

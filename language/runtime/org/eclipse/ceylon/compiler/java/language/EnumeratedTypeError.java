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
 * <p>Thrown when an unexpected subclass of a enumerated type is encountered, 
 * for example in an exhaustive {@code switch} statement.</p>
 * 
 * <p>The subtypes of enumerated types are normally verified at compile time. 
 * This error can only occur if the compiler's checks are subverted, for 
 * example by subclassing an enumerated type in Java code, or by adding a 
 * subclass without recompiling dependent code.</p>
 *  
 * @author tom
 */
@Ceylon(major = 8)
@Class
public class EnumeratedTypeError extends Error {

    private static final long serialVersionUID = 4496076366345444435L;

    public EnumeratedTypeError(String message) {
        super(message);
    }

}

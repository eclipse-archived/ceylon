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
 * <p>Thrown when a method has been declared, but not specified.</p>
 * 
 * <p>The definite assignment of methods with deferred specification is 
 * normally verified at compile time. 
 * This error can only occur if the compiler's checks are subverted, for 
 * example by using Java-level reflection to invoke a {@code private} 
 * method which was left uninitialized.</p>
 *  
 * @author tom
 */
@Ceylon(major = 8)
@Class
public class UninitializedMethodError extends Error {

    private static final long serialVersionUID = 7063698116161174567L;

    public UninitializedMethodError() {
        super();
    }

}

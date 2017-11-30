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
 * <p>Thrown when something is invoked which 
 * we statically know cannot be invoked. Such as an
 * abstract constructor which was never delegated-to.</p>
 *  
 * @author tom
 */
@Ceylon(major = 8)
@Class
public class UninvokableError extends Error {

    private static final long serialVersionUID = -8438042507968776L;

    public UninvokableError() {
        super();
    }

}

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
 * Used for captured {@code variable} local attributes. 
 */
public class VariableBoxDouble
        implements java.io.Serializable{
    
    private static final long serialVersionUID = -865429594096963247L;
    
    public double ref;
    
    public VariableBoxDouble() {
        ref = 0.0;
    }
    
    public VariableBoxDouble(double ref) {
        this.ref = ref;
    }
}

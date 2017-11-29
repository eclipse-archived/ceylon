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
public class VariableBoxLong
        implements java.io.Serializable {
    
    private static final long serialVersionUID = -6894579505815283454L;
    
    public long ref;
    
    public VariableBoxLong() {
        ref = 0L;
    }
    
    public VariableBoxLong(long ref) {
        this.ref = ref;
    }
    
}

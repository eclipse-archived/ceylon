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

import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

/**
 * Baseclass for all type constructors.
 */
public abstract class AbstractTypeConstructor {
    
    private final String string;

    public AbstractTypeConstructor(String string) {
        this.string = string;
    }
    
    /**
     * Apply the given types to this type constructor
     * @param types
     * @return
     */
    public abstract Object apply(TypeDescriptor[] types);
    
    public String toString() {
        return string;
    }
}
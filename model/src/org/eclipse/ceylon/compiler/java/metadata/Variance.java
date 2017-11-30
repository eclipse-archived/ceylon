/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.metadata;

/**
 * Enumerates possible values for {@link TypeParameter#variance @TypeParameter.variance}
 */
public enum Variance {
    /** Contravariant ({@code in}) type parameter */
    IN("in"), 
    /** Conavariant ({@code out}) type parameter */
    OUT("out"),
    /** Invariant type parameter */
    NONE("");
    
    private final String pretty;
    Variance(String pretty) {
        this.pretty = pretty;
    }
    public String getPretty() {
        return pretty;
    }
}

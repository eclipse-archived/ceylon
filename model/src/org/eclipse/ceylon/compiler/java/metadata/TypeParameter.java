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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A type parameter of a Ceylon type or method (should be contained in an
 * {@link TypeParameters @TypeParameters()}).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface TypeParameter {
    
    /** The name of the type parameter */
    String value();
    
    /** 
     * String representation of the types that this type parameter is 
     * constrained to satisfy (in the {@code given ... satisfies ...} clause). 
     * May contain fully-qualified type names and type parameter names 
     */
    String[] satisfies() default {};

    /** 
     * String representation of the case types that this type parameter is 
     * constrained to be (in the {@code given ... of ...} clause). 
     * May contain fully-qualified type names and type parameter names 
     */
    String[] caseTypes() default {};

    /** The variance of this type parameter */
    Variance variance() default Variance.NONE;
    
    /**
     * The default value for this type parameter if it is optional and defaulted.
     * Defaults to the empty string if this type parameter is not optional. 
     * May contain fully-qualified type names and type parameter names.
     */
    String defaultValue() default "";
}

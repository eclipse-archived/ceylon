/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
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

/**
 * Annotation applied to member types who have been extracted out of their containers.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Container {
    
    /**
     * The container class.
     */
    java.lang.Class<?> klass();
    
    /**
     * Is it a static member type?
     */
    boolean isStatic() default false;
}

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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation applied to a Java class 
 * also annotated with {@link Attribute @Attribute} which holds a runtime 
 * module descriptor instance.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Module {
    /** The version of the module */
    public String version();
    /** The name of the module */
    public String name();
    /** The readable name of the module */
    public String label() default "";
    /** The module documentation. */
    public String doc() default "";
    /** The module author(s). */
    public String[] by() default {};
    /** The module license */
    public String license() default "";
    /** The module dependencies */
    public Import[] dependencies() default {};
    /** The module native backends. */
    public String[] nativeBackends() default {};
    /** The module group */
    public String group() default "";
    /** The module artifact */
    public String artifact() default "";
}
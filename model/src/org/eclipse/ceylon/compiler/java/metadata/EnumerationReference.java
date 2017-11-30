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
 * Annotation for methods of annotation types, encoded as a java.lang.Class, 
 * that are actually a reference to an anonymous class that's a case of an enumerated type.
 * This is not used for booleans though. 
 * <pre>
 * @interface Foo$annotation {
 *     @.org.eclipse.ceylon.compiler.java.metadata.EnumerationReference
 *     public abstract .java.lang.Class<? extends Bar> bar();
 * }
 * </pre>
 * @author tom
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface EnumerationReference {
}

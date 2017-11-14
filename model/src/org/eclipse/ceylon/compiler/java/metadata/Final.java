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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * In EE mode we don't generate {@code final} methods, but the model loader
 * uses Java {@code final} to infer Ceylon {@code default}. So in EE mode we annotate 
 * the non-{@code final}, non-{@code default} methods with @Final.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Final {

}

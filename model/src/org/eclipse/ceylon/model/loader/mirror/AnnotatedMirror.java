/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.loader.mirror;

import java.util.Set;

/**
 * Represents an annotated program element (class, method, constructor, field, parameter)
 * 
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface AnnotatedMirror {

    /**
     * Returns the program element's name
     */
    String getName();

    /**
     * Gets an annotation by annotation type name (fully qualified annotation class name)
     */
    AnnotationMirror getAnnotation(String type);

    /**
     * Gets all annotation names
     */
    Set<String> getAnnotationNames();
}

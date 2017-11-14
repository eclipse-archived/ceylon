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

/**
 * Represents an annotation
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface AnnotationMirror {

    /**
     * Returns the annotation value of the given annotation field. The value should be wrapped as such:
     * 
     * - String for a string value
     * - boxed value for a primitive value (Integer, Character…)
     * - TypeMirror for a class value
     * - AnnotationMirror for an annotation value
     * - List for an array (the array elements must be wrapped using the same rules)
     */
    Object getValue(String fieldName);

    /**
     * Returns the value of the "value" field
     */
    Object getValue();

}

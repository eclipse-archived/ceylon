/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.metamodel;

import org.eclipse.ceylon.compiler.java.metadata.Ignore;

import ceylon.language.Annotated;

/**
 * Interface for {@code Free*} and {@code Applied*} implementors of 
 * {@code ceylon.language.meta.model.Annotated} which gets the 
 * Java annotations from the underyling Java reflection object.
 * @author tom
 */
public interface AnnotationBearing extends Annotated {

    static final java.lang.annotation.Annotation[] NONE = new java.lang.annotation.Annotation[0];
    
    /**
     * @return the Java annotations for this Annotated
     */
    @Ignore
    java.lang.annotation.Annotation[] $getJavaAnnotations$();
    
    @Ignore
    boolean $isAnnotated$(java.lang.Class<? extends java.lang.annotation.Annotation> annotationType);
    
}

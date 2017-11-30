/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.loader.mirror;

import java.util.List;

/**
 * Represents a method.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface MethodMirror extends AnnotatedMirror, AccessibleMirror {

    /**
     * Returns true if this method is static
     */
    boolean isStatic();

    /**
     * Returns true if this method is a constructor
     */
    boolean isConstructor();

    /**
     * Returns true if this method is abstract
     */
    boolean isAbstract();
    
    /**
     * Is this a {@code default} method of a java interface.
     * Note the difference with {@link #isDefault()}.
     */
    boolean isDefaultMethod();
    
    /**
     * Returns true if this method is final
     */
    boolean isFinal();

    /**
     * Returns true if this method is a static initialiser
     */
    boolean isStaticInit();

    /**
     * Returns true if this method is variadic
     */
    boolean isVariadic();
    
    /**
     * Returns the list of parameters
     */
    List<VariableMirror> getParameters();

    /**
     * Returns the return type for this method 
     */
    TypeMirror getReturnType();
    
    boolean isDeclaredVoid();

    /**
     * Returns the list of type parameters for this method
     */
    List<TypeParameterMirror> getTypeParameters();
    
    /**
     * If this is a method on an annotation type, whether the method has a 
     * {@code default} expression;
     * Note the difference with {@link #isDefaultMethod()}.
     */
    boolean isDefault();
    
    /**
     * Return this method's enclosing class.
     */
    ClassMirror getEnclosingClass();
}

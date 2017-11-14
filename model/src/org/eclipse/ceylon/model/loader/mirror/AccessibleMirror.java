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
 * Represents an program element (class, method, constructor, field) with visibility access restrictions
 * 
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface AccessibleMirror {
    
    /**
     * Returns true if the element is public
     */
    boolean isPublic();

    /**
     * Returns true if the element is protected
     */
    boolean isProtected();

    /**
     * Returns true if the element is package-protected
     */
    boolean isDefaultAccess();

}

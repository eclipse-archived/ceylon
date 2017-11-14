/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.loader.model;

import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Scope;

/**
 * Scope used to contain local declarations.
 * 
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface LocalDeclarationContainer extends Scope {
    
    /**
     * Gets a local declaration by (prefixed) name
     */
    Declaration getLocalDeclaration(String name);
    
    /**
     * Adds a new local declaration. Its prefixed name must be unique.
     */
    void addLocalDeclaration(Declaration decl);
}

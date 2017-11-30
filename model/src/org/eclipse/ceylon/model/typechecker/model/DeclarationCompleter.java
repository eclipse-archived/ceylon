/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.typechecker.model;

/**
 * Completer for declarations that will only complete certain 
 * fields which are expensive to compute, on demand.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface DeclarationCompleter {

    /**
     * Completes the <tt>actual</tt> and <tt>refinedDeclaration</tt> 
     * members of the given declaration.
     */
    public void completeActual(Declaration decl);
}

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

import java.util.List;

/**
 * Represents a type parameter.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface TypeParameterMirror {

    /**
     * Returns the name of the type parameter.
     */
    String getName();

    /**
     * Returns the list of bounds for this type parameter.
     */
    List<TypeMirror> getBounds();

}

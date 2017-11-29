/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.cmr;

/**
 * Abstraction over the CMR Repository type
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface Repository {
    /**
     * Returns a display string that represents this Repository
     */
    String getDisplayString();

    /**
     * Returns the name of the repository namespace.
     * <code>null</code> means the default Ceylon namespace.
     */
    String getNamespace();
}

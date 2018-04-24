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
 * Any program element with a name - including declarations,
 * packages, and modules.
 * 
 * @author Gavin King
 *
 */
public interface Referenceable extends Sourced {
    public String getNameAsString();
}

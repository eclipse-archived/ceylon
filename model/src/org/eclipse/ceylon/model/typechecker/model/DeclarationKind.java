/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.typechecker.model;

/**
 * An unscientific categorization of the kinds of
 * declarations we have (for use in heuristic 
 * comparisons in the IDE). Note that this 
 * classification doesn't really relate to anything
 * in the language spec.
 * 
 * @author Gavin King
 */
public enum DeclarationKind {
    TYPE, TYPE_PARAMETER, MEMBER, SETTER, CONSTRUCTOR
}

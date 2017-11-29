/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A type parameter declaration."
shared sealed interface TypeParameter satisfies Declaration {
    
    // FIXME: make it NestableDeclaration&GenericDeclaration?
    "The declaration that declared this type parameter. This is either a [[ClassOrInterfaceDeclaration]] or a
     [[FunctionDeclaration]]."
    shared formal NestableDeclaration container;
    
    "True if this type parameter has a default type argument and can be omitted."
    shared formal Boolean defaulted;
    
    "This type parameter's default type argument, if it has one."
    shared formal OpenType? defaultTypeArgument;
    
    "This type parameter's variance, as defined by `in` or `out` keywords."
    shared formal Variance variance;
    
    "The `satisfies` upper bounds for this type parameter."
    shared formal OpenType[] satisfiedTypes;

    "The `of` enumerated bounds for this type parameter."
    shared formal OpenType[] caseTypes;
}
/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A declaration which can be contained in a [[Package]] or in another [[NestableDeclaration]].
 
 Functions, values, classes, interfaces and aliases are such declarations."
shared sealed interface NestableDeclaration of 
                                        FunctionOrValueDeclaration |
                                        ClassOrInterfaceDeclaration |
                                        ConstructorDeclaration |
                                        SetterDeclaration |
                                        AliasDeclaration
        satisfies AnnotatedDeclaration & TypedDeclaration {

    "True if this declaration is annotated with [[actual|ceylon.language::actual]]."
    shared formal Boolean actual;

    "True if this declaration is annotated with [[formal|ceylon.language::formal]]."
    shared formal Boolean formal;

    "True if this declaration is annotated with [[default|ceylon.language::default]]."
    shared formal Boolean default;

    "True if this declaration is annotated with [[shared|ceylon.language::shared]]."
    shared formal Boolean shared;
    
    "True if this declaration is annotated with [[static|ceylon.language::static]]."
    shared formal Boolean static;
    
    "This declaration's package container."
    shared formal Package containingPackage;
    
    "This declaration's module container."
    shared formal Module containingModule;
    
    "This declaration's immediate container, which can be either a [[NestableDeclaration]]
     or a [[Package]]."
    shared formal NestableDeclaration|Package container;
    
    "True if this declaration is a toplevel declaration."
    shared formal Boolean toplevel;
    
}
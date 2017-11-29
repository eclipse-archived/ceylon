/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A setter declaration represents the assign block of a [[ValueDeclaration]]."
shared sealed interface SetterDeclaration
        satisfies NestableDeclaration {

    "The variable value this setter is for."
    shared formal ValueDeclaration variable;

    actual => variable.actual;
    
    formal => variable.formal;

    default => variable.default;
    
    shared => variable.shared;

    containingPackage => variable.containingPackage;
    
    containingModule => variable.containingModule;
    
    container => variable.container;

    toplevel => variable.toplevel;
}

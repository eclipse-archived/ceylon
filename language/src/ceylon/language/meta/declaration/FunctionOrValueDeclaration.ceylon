/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A function or value declaration."
shared sealed interface FunctionOrValueDeclaration
    of FunctionDeclaration
     | ValueDeclaration 
    satisfies NestableDeclaration {
    
    "True if this function or value is a parameter to a [[FunctionalDeclaration]]."
    shared formal Boolean parameter;
    
    "True if this function or value is a parameter and has a default value."
    shared formal Boolean defaulted;
    
    "True if this function or value is a parameter and is variadic (accepts a list of values)."
    shared formal Boolean variadic;
}
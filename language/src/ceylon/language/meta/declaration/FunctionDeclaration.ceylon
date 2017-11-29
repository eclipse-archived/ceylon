/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.model{
    Type,
    Function, 
    Method
}

"""Abstraction over declarations which can be invoked, namely functions, methods and constructors """
shared sealed interface FunctionDeclaration
        satisfies FunctionOrValueDeclaration & FunctionalDeclaration {
    
    shared actual formal Function<Return, Arguments> apply<Return=Anything, Arguments=Nothing>(Type<>* typeArguments)
            given Arguments satisfies Anything[];
    
    shared actual formal Method<Container, Return, Arguments> memberApply<Container=Nothing, Return=Anything, Arguments=Nothing>(Type<Object> containerType, Type<>* typeArguments)
            given Arguments satisfies Anything[];
}

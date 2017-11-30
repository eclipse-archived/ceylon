/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.declaration{CallableConstructorDeclaration}


"A model for a callable constructor of a member class."
see(interface MemberClassValueConstructor)
since("1.2.0")
shared sealed interface MemberClassCallableConstructor<in Container=Nothing, out Type=Object, in Arguments=Nothing>
        satisfies FunctionModel<Type, Arguments> & Qualified<CallableConstructor<Type, Arguments>, Container>
        given Arguments satisfies Anything[] {
    
    
    "This constructor's declaration."
    shared formal actual CallableConstructorDeclaration declaration;
    
    shared formal actual MemberClass<Container, Type> type;
    
    "The class containing this constructor; the type of instances produced 
     by this constructor."
    shared actual formal ClassModel<Type> container;
    
    shared actual formal CallableConstructor<Type, Arguments> bind(Anything container);
}

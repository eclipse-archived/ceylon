/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.declaration{ValueConstructorDeclaration}

"A model for a value constructor of a member class."
since("1.2.0")
shared sealed interface MemberClassValueConstructor<in Container=Nothing, out Type=Object>
        satisfies ValueModel<Type> & Qualified<ValueConstructor<Type>, Container> {
    
    "This value's declaration."
    shared formal actual ValueConstructorDeclaration declaration;
    
    "This value's closed type."
    shared formal actual MemberClass<Container, Type> type;
    
    "The class containing this constructor; the type of instances produced 
     by this constructor."
    shared actual formal ClassModel<Type> container;
    
    "Binds this attribute to the given container instance. The instance type is checked at runtime."
    throws(class StorageException,
        "If this attribute is not stored at runtime, for example if it is neither shared nor captured.")
    shared actual formal ValueConstructor<Type> bind(Anything container);
}
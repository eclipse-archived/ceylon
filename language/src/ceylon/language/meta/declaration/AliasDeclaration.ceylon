/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Type alias declaration. While type aliases are erased (substituted for what they alias is a better term) from every 
 declaration that uses them during compile-time, the declaration of the type alias is still visible at run-time."
shared sealed interface AliasDeclaration 
    satisfies NestableDeclaration & GenericDeclaration {

    "The open type that is substituted by this type alias."
    shared formal OpenType extendedType;

    /*
    FIXME: this is too shaky WRT member types, we'll figure it out later    
    shared formal AppliedType<Type> apply<Type=Anything>(AppliedType<Anything>* typeArguments);
    
    shared formal AppliedType<Type> & Member<Container, AppliedType<Type> & Model> memberApply<Container=Nothing, Type=Anything>(AppliedType<Container> containerType, AppliedType<Anything>* typeArguments);
*/
}

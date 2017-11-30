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
    ValueConstructor,
    IncompatibleTypeException, 
    StorageException,
    MemberClassValueConstructor
}
import ceylon.language.meta.declaration {
    CallableConstructorDeclaration
}

"""Declaration model for value constructors, for example
   
       class Currency {
           "The US Dollar"
           shared new usd {}
           // ...
       }
       
       ValueConstructorDeclaration dollars = `new Currency.usd`;
   """
see (interface CallableConstructorDeclaration)
since("1.2.0")
shared sealed interface ValueConstructorDeclaration 
        satisfies GettableDeclaration & ConstructorDeclaration {
    
    "The class this constructor constructs"
    shared actual formal ClassDeclaration container; 
    
    "Apply the given closed type argument to this toplevel value constructor 
     to obtain as value constructor model."
    shared formal ValueConstructor<Result> apply<Result=Anything>();
    
    "Apply the given closed type argument to this member value constructor 
     to obtain as value constructor model."
    shared formal MemberClassValueConstructor<Container, Result> memberApply
            <Container=Nothing, Result=Anything>
            (Type<Object> containerType);
    
    "Reads the current value of this toplevel value."
    shared actual default Object get()
            => apply<Object>().get();
    
    "Reads the current value of this attribute on the given container instance."
    throws(class IncompatibleTypeException, 
        "If the specified container is not compatible with this attribute.")
    throws(class StorageException,
        "If this attribute is not stored at runtime, for example if it is 
         neither shared nor captured.")
    shared actual default Object memberGet(Object container)
            => memberApply<Nothing, Object>(`Nothing`).bind(container).get();
    
    /*"Sets the current value of this toplevel value."
     shared actual default void set(Nothing newValue) {
        throw;
     }
     
     "Sets the current value of this attribute on the given container instance."
     throws(`class IncompatibleTypeException`, "If the specified container or new value type is not compatible with this attribute.")
     throws(`class StorageException`,
        "If this attribute is not stored at runtime, for example if it is neither shared nor captured.")
     shared actual formal void memberSet(Object container, Nothing newValue) {
        throw;
     }*/
    
    /*shared actual formal Value<Get,Nothing> apply<Get,Set>();
     
     shared actual formal Attribute<Container,Get,Nothing> memberApply<Container,Get,Set>(Type<Object> containerType);*/
}
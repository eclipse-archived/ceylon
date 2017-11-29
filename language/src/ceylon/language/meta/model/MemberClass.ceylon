/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"""A class model represents the model of a Ceylon class that you can instantiate and inspect.
   
   A member class is is declared on classes or interfaces.
   
   This is both a [[ClassModel]] and a [[Member]]: you can invoke it with an instance value
   to bind it to that instance and obtain a [[Class]]:

       shared class Outer(String name){
           shared class Inner(){
               shared String hello => "Hello "+name;
           }
       }
       
       void test(){
           MemberClass<Outer,Outer.Inner,[]> memberClass = `Outer.Inner`;
           Class<Outer.Inner,[]> c = memberClass(Outer("Stef"));
           // This will print: Hello Stef
           print(c().hello);
       }
 """
shared sealed interface MemberClass<in Container=Nothing, out Type=Anything, in Arguments=Nothing>
        satisfies ClassModel<Type, Arguments> & Member<Container, Class<Type, Arguments>>
        given Arguments satisfies Anything[] {
    
    shared actual formal Class<Type, Arguments> bind(Anything container);
    
    shared actual formal MemberClassCallableConstructor<Container, Type, Arguments>? defaultConstructor;
    
    "The constructor with the given name, or null if this class lacks 
     a constructor of the given name"
    shared actual formal MemberClassCallableConstructor<Container,Type,Arguments>|MemberClassValueConstructor<Container,Type>? getConstructor<Arguments>(String name)
        given Arguments satisfies Anything[];
    /*shared actual formal MemberClassConstructor<Container,Type, Arguments>? getConstructor<Arguments=Nothing>(String name)
            given Arguments satisfies Anything[];*/
}

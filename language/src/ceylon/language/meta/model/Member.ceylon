/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.model { AppliedType = Type }

"""Model for members that can be bound to a containing instance to turn them into toplevel models.
   
   You can bind a member to an instance by invoking that member with the instance as parameter:
   
       shared class Outer(String name){
           shared class Inner(){
               shared String hello => "Hello "+name;
           }
       }
       
       void test(){
           Member<Outer,Class<Outer.Inner,[]>> memberClass = `Outer.Inner`;
           Class<Outer.Inner,[]> c = memberClass(Outer("Stef"));
           // This will print: Hello Stef
           print(c().hello);
       }
   """
shared sealed interface Member<in Container=Nothing, out Kind=Model>
        satisfies Qualified<Kind,Container> 
        given Kind satisfies Model {
    
    "The declaring closed type. This is the type that declared this member."
    shared formal AppliedType<> declaringType;
    
    /*"Type-unsafe container binding, to be used when the container type is unknown until runtime.
     
     This has the same behaviour as invoking this `Member` directly, but exchanges compile-time type
     safety with runtime checks."
    throws(`class IncompatibleTypeException`, "If the container is not assignable to this member's container")
    shared formal Kind bind(Object container);*/
}
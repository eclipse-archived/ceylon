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

"""A callable constructor model represents the model of a Ceylon class 
   constructor that you can invoke and inspect.
   
   ## Callablity
   
   As with [[Function]] you can also invoke a `CallableConstructor`, doing so 
   instantiates an instance:
   
        shared class Foo {
            shared String name;
            shared new foo(String name) {
                this.name = name;
            }
        }
        
        void test() {
            Constructor<Foo,[String]> ctor = `Foo.foo`;
            // This will print: Stef
            print(ctor("Stef").name);
        }
        
   ## Genericity
        
   This class inherits [[Generic]] but a constructor in Ceylon cannot 
   have a type parameters. 
   For symmetry with [[CallableConstructorDeclaration.apply]] the 
   [[typeArguments]] and [[typeArgumentList]] refer to the type arguments 
   of the constructor's class.
   """
since("1.2.0")
shared sealed interface CallableConstructor<out Type=Object, in Arguments=Nothing>
        satisfies FunctionModel<Type, Arguments> & Applicable<Type, Arguments>
        given Arguments satisfies Anything[] {
    
    "This constructor's declaration."
    shared formal actual CallableConstructorDeclaration declaration;
    
    shared formal actual ClassModel<Type> type;
    
    "The class containing this constructor; the type of instances produced 
     by this constructor."
    shared actual formal ClassModel<Type>? container;
    
}


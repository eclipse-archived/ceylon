/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.declaration{FunctionDeclaration}

"""A function model represents the model of a Ceylon function that you can invoke and inspect.
   
   A function is a toplevel binding, declared on a package.
   
   This is a [[FunctionModel]] that you can also invoke:
   
       shared String foo(String name) => "Hello "+name;
       
       void test(){
           Function<String,[String]> f = `foo`;
           // This will print: Hello Stef
           print(f("Stef"));
       }
 """
shared sealed interface Function<out Type=Anything, in Arguments=Nothing>
        satisfies FunctionModel<Type, Arguments> & Applicable<Type, Arguments>
        given Arguments satisfies Anything[] {
    "This function's declaration."
    shared formal actual FunctionDeclaration declaration;
}

/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"""The declaration model of a class that has constructors. For example:
   
       class Point {
           shared new(Float x, Float y) {
               // ...
           }
           shared new polar(Float r, Float theta) {
               // ...
           }
           shared new origin {
               // ...
           }
       }
       
   Such classes may not have a default (unnamed) constructor,
   so [[defaultConstructor|ClassDeclaration.defaultConstructor]] 
   has optional type.
   """
see(interface ClassWithInitializerDeclaration)
since("1.2.0")
shared sealed interface ClassWithConstructorsDeclaration 
        satisfies ClassDeclaration {
}
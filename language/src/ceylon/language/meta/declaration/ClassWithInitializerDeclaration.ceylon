/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language { AnnotationType = Annotation }


"""The declaration model of a class that has a parameter list rather than
   explicit constructors. For example:
   
       class Color(Integer rgba) {
       }
   
   Such classes have a meaningful parameter list and for abstraction purposes
   have a single [[defaultConstructor]] representing the
   class' parameter list and the class initializer code.
   This "constructor" will have the same 
   [[SharedAnnotation]], [[DeprecationAnnotation]] 
   and [[ThrownExceptionAnnotation]]
   annotations as the class, but will have no 
   other annotations.
   """
see(interface ClassWithConstructorsDeclaration)
since("1.2.0")
shared sealed interface ClassWithInitializerDeclaration 
        satisfies ClassDeclaration {
    
    "A CallableConstructorDeclaration representing the class initializer."
    shared actual formal CallableConstructorDeclaration defaultConstructor;
    
    "The list of parameter declarations for this class."
    shared actual formal FunctionOrValueDeclaration[] parameterDeclarations;
    
    "A singleton sequence containing the [[defaultConstructor]]."
    shared actual default Sequence<CallableConstructorDeclaration> constructorDeclarations() 
            => Singleton(defaultConstructor);
    
    shared actual default CallableConstructorDeclaration? getConstructorDeclaration(String name) 
            => if (name.empty) then defaultConstructor else null;
    
    shared actual formal CallableConstructorDeclaration[] annotatedConstructorDeclarations<Annotation>()
            given Annotation satisfies AnnotationType;
}

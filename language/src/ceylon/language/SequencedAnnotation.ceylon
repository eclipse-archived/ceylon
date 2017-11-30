/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"""An annotation that may occur multiple times at a given 
   program element, and only on certain program elemenets.
 
   A sequenced annotation is declared simply by having the 
   annotation class satisfy `SequencedAnnotation` instead of 
   [[Annotation]]. For example the following would only be 
   allowed on `class` declarations, functions or methods:
 
       alias ExecutableDeclaration 
              => ClassOrInterfaceDeclaration|FunctionDeclaration;
       "Documents a pattern in which the annotated element 
        particpates."
       shared final annotation class Pattern(String name) 
               satisfies SequencedAnnotation<Pattern, ExecutableDeclaration> {}
   
   At runtime a [[ceylon.language.meta.declaration::Declaration]] 
   instance can be queried for its `SequencedAnnotation`s of 
   a certain type using [[ceylon.language.meta::annotations]] 
   or [[ceylon.language.meta::sequencedAnnotations]]."""
see(interface Annotation)
shared interface SequencedAnnotation<out Value, 
            in ProgramElement=Annotated,
            out Type=Anything>
        of Value
        satisfies ConstrainedAnnotation
            <Value,Value[],ProgramElement,Type>
        given Value satisfies SequencedAnnotation
            <Value,ProgramElement,Type>
        given ProgramElement satisfies Annotated {}

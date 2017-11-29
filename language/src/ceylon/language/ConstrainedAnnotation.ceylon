/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"An annotation constrained to appear only on certain program 
 elements, and only with certain values. 
 
 This interface should never be satisfied directly by any
 annotation type. Instead, either [[OptionalAnnotation]] or 
 [[SequencedAnnotation]] should be satisfied by the 
 annotation type.
 
 The type parameters encode information about the annotation
 type and its constraints:
 
  - [[Value]] represents the type of the annotation itself, 
  - [[ProgramElement]] represents a constraint on the  
    _reference expression type_ of the annotated program 
    element, for example, 
    [[ceylon.language.meta.declaration::ClassDeclaration]] 
    or [[ceylon.language.meta.declaration::Module]], where
    [[Annotated]] means there is no constraint, and
  - [[Type]] is a constraint on the _metamodel type_ of the 
    annotated program element, for example, 
    [[`Function<Float,[Float,Float]>`
     |ceylon.language.meta.model::Function]], 
    where `Anything` means there is no constraint, and that 
    the program element need not have a metamodel type."
see (interface Annotation,
     interface OptionalAnnotation,
     interface SequencedAnnotation)
shared interface ConstrainedAnnotation<out Value=Annotation, 
            out Values=Anything, in ProgramElement=Nothing,
            out Type=Anything> 
        //of Value
        //Note: adding the following constraint would
        //      make ConstrainedAnnotation a GADT, which
        //      the language does not currently support 
        //of OptionalAnnotation<Value,ProgramElement> | 
        //   SequencedAnnotation<Value,ProgramElement>
        satisfies Annotation
        given Value satisfies Annotation
        given ProgramElement satisfies Annotated {

}

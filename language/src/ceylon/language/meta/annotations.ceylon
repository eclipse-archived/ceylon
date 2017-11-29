/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.model { Class }

/*
 * Do we really need to enforce that you can't 
 * ask for the annotations of a certain type for a
 * certain program element unless the annotation 
 * type can appear at that program element? Why not
 * just return no annotations?
 * 
 */

"The annotations of the [[given type|annotationType]] 
 applied to the [[given program element|programElement]]. 
 For example:
 
     // Does the process declaration have any annotations
     // of type SharedAnnotation?
     value isShared = annotations(`SharedAnnotation`, `value process`) exists;
 
 The annotations may be returned in any order."
shared native Values annotations<Value, Values, in ProgramElement>(
    Class<ConstrainedAnnotation<Value,Values,ProgramElement>> annotationType,
    ProgramElement programElement)
        given Value satisfies ConstrainedAnnotation<Value,Values,ProgramElement>
        //given Values of <Value?> | <Value[]>
        given ProgramElement satisfies Annotated;

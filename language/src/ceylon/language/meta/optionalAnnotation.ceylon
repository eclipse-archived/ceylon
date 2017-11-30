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
// Do we really need this? Isn't annotations sufficient?
"The value of given optional annotation type on the given program element, 
 or null if the program element was not annotated with that annotation type.
 For example:
 
     // Does the process declaration have the Shared annotation?
     value isShared = optionalAnnotation(`SharedAnnotation`, `value process`) exists;
 "
shared Value? optionalAnnotation<Value, in ProgramElement>(
            Class<OptionalAnnotation<Value,ProgramElement>> annotationType,
            ProgramElement programElement)
        given Value satisfies OptionalAnnotation<Value,ProgramElement>
        given ProgramElement satisfies Annotated { 
    return annotations<Value,Value?,ProgramElement>(annotationType, programElement);
}
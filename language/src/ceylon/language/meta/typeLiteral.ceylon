/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.model { ClosedType = Type }

"Functional equivalent to type literals. Allows you to get a closed type instance
 for a given type argument.

 For example:

     assert(is Interface<List<Integer>> listOfIntegers = typeLiteral<List<Integer>>());
 "
shared native ClosedType<Type> typeLiteral<out Type>()
    given Type satisfies Anything;

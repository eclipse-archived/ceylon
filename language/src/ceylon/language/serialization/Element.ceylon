/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"""
   An [[Array]] instance referring to another instance via one 
   of its elements.
 
   For example, given:
 
       value arr = Array({"hello"});
       value context = serialization();
       value refs = context.references(arr);
       assert(is Element elementRef = refs.find((element) => element is Element));
       assert(elementRef.referred(arr) == "hello");
       assert(elementRef.index == 0);
"""
shared sealed interface Element /*<Instance>*/
        satisfies ReachableReference /*<Instance>*/{
    "The index of the element in the Array which makes the reference."
    shared formal Integer index;
}

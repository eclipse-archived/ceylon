/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Given two [[Comparable]] values, return smallest of the two.
 
 If exactly one of the given values violates the reflexivity 
 requirement of [[Object.equals]] such that `x!=x`, then the 
 other value is returned. In particular, if exactly one is 
 an [[undefined `Float`|Float.undefined]], it is not 
 returned.
 
 _On the JVM platform, for arguments of type `Integer` or 
 `Float`, prefer [[Integer.smallest]] or [[Float.smallest]]
 in performance-sensitive code._"
see (interface Comparable, 
     function largest, 
     function min, 
     function Integer.smallest,
     function Float.smallest)
tagged("Comparisons")
shared Element smallest<Element>(Element x, Element y) 
        given Element satisfies Comparable<Element> 
        => if (x!=x || y<x) then y else x;

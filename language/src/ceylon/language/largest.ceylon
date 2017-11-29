/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Given two [[Comparable]] values, return largest of the two.
 
 If exactly one of the given values violates the reflexivity 
 requirement of [[Object.equals]] such that `x!=x`, then the 
 other value is returned. In particular, if exactly one is 
 an [[undefined `Float`|Float.undefined]], it is not 
 returned.
 
 _On the JVM platform, for arguments of type `Integer` or 
 `Float`, prefer [[Integer.largest]] or [[Float.largest]]
 in performance-sensitive code._"
see (interface Comparable, 
     function smallest, 
     function max, 
     function Integer.largest,
     function Float.largest)
tagged("Comparisons")
shared Element largest<Element>(Element x, Element y) 
        given Element satisfies Comparable<Element> 
        => if (x!=x || y>x) then y else x;

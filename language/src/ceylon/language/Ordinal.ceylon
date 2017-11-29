/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Abstraction of _ordinal types_, that is, of types where 
 each instance has a [[successor]] and [[predecessor]], such 
 as:
  
 - types which represent or are isomorphic to the 
   mathematical integers, for example, [[Integer]] and other 
   [[Integral]] numeric types, and even [[Character]], along 
   with
 - enumerated types which are isomorphic to the mathematical
   integers under modular arithmetic, for example, the days
   of the week, and
 - enumerated types which are isomorphic to a bounded range 
   of integers, for example, a list of priorities.
 
 The _increment_ operator `++` and _decrement_ operator `--`
 are defined for all types which satisfy `Ordinal`.
 
     function increment() {
         count++;
     }
 
 Many ordinal types have a [[total order|Comparable]]. If an
 ordinal type has a total order, then it should satisfy:
 
 - `x.successor >= x`, and
 - `x.predecessor <= x`.
 
 An ordinal enumerated type `X` with a total order has 
 well-defined `maximum` and `minimum` values where
 `minimum<x<maximum` for any other instance `x` of `X`.
 Then the `successor` and `predecessor` operations should
 satisfy:
 
 - `minimum.predecessor==minimum`, and
 - `maximum.successor==maximum`."
see (class Character, 
     class Integer, 
     interface Integral, 
     interface Comparable,
     interface Enumerable)
by ("Gavin")
tagged("Numbers")
shared interface Ordinal<out Other> of Other
        given Other satisfies Ordinal<Other> {
    
    "The successor of this value."
    shared formal Other successor;
    
    "The predecessor of this value."
    shared formal Other predecessor;
    
}

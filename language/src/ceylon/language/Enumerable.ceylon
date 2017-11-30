/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Abstraction of [[ordinal types|Ordinal]] whose values may 
 be used as endpoints of a [[span]] or [[measure]].
 
 An `Enumerable` type is characterized by each element 
 having well-defined [[offset]] and [[neighbour]] functions.
 Given an instance `x` of an enumerable type `X`:
 
 - for any integer-valued offset, there is a unique 
   _neighbour_ `y` of `X` with that offset, and
 - if `y` is an instance of `X`, then there is a
   well-defined integer-valued _offset_ of `x` from `y`.
 
 The offset function must satisfy:
 
 - `x.offset(x) == 0`, and
 - `x.successor.offset(x) == 1` if `x!=x.successor`.
 
 The neighbour function must satisfy:
 
 - `x.neighbour(0) == x`,
 - `x.neighbour(n-1) == x.neighbour(n).predecessor`, and
 - `x.neighbour(n+1) == x.neighbour(n).successor`.
 
 Of course, it follows that:
 
 - `x.neighbour(-1) == x.predecessor`, and
 - `x.neighbour(1) == x.successor`.
 
 An enumerable type may be _linear_ or _recursive_. If `X` 
 is a linear enumerable type, then the offset function 
 satisfies:
 
 - `x.predecessor.offset(x) == -1` if `x!=x.predecessor`,
 - `x.offset(y) == -y.offset(x)` for any instance `y` of `X`, 
   and
 - `x.offset(y) == x.offset(z) + z.offset(y)`.
 
 Otherwise, `X` is a recursive enumerable type with a finite
 list of enumerated instances of size `count`, and its 
 offset and neighbour functions must satisfy:
 
 - `x.neighbour(count)==x`,
 - `x.offset(y) >= 0` for any instance `y` of `X`, and 
 - `x.predecessor.offset(x) == count - 1`.
 
 A range of values of an enumerable type may be specified 
 using:
 
 - the _span operator_, written `first..last`, or 
 - the _segment operator_, written `first:length`."
see (class Range, 
     function span, function measure)
shared interface Enumerable<Other> of Other
        satisfies Ordinal<Other>
        given Other satisfies Enumerable<Other> {
    
    "The indirect successor or predecessor at the given
     [[offset]], where:
     
     - `x.neighbour(0) == x`,
     - `x.neighbour(i+1) == x.neighbour(i).successor`, and
     - `x.neighbour(i-1) == x.neighbour(i).predecessor`."
    throws (class OverflowException, 
            "if the neighbour cannot be represented as an 
             instance of the type")
    since("1.1.0")
    shared formal Other neighbour(Integer offset);
    
    shared actual default Other successor => neighbour(1);
    shared actual default Other predecessor => neighbour(-1);
    
    "Compute the offset from the given value, where:
     
     - `x.offset(x) == 0`, and
     - `x.successor.offset(x) == 1` if `x!=x.successor`."
    throws (class OverflowException,
            "if the offset cannot be represented as an 
             integer")
    since("1.1.0")
    shared formal Integer offset(Other other);
    
    "The sign of the offset from the given value."
    since("1.1.0")
    shared default Integer offsetSign(Other other)
            => offset(other).sign;
    
}

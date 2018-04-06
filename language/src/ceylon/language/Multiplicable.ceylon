/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"""Abstraction of types which support a multiplication
   operator `x * y`. For [[numeric types|Numeric]], this is
   just familiar numeric multiplication.
   
       Float c = 2 * pi * r;
   
   A concrete class that implements this interface should be 
   a mathematical _semigroup_. That is, the multiplication 
   operation should be associative, satisfying:
   
   - `(x*y)*z == x*(y*z)`
   
   A `Multiplicable` type might be a _monoid_, that is, a 
   semigroup with an additive identity element, usually 
   denoted `1`, but this is not required. For example:
   
   - `Float` is a monoid with identity element `1.0`, and 
   - `Integer` is a monoid with identity element `1`.
   
   For any monoid, the multiplication operation must satisfy:
   
   - `x * 1 == x`"""
see (interface Numeric)
by ("Gavin")
tagged("Numbers")
shared interface Multiplicable<Other> of Other
        given Other satisfies Multiplicable<Other> {
    
    "The result of multiplying the given value by this 
     value. This operation should never perform any kind of 
     mutation upon either the receiving value or the 
     argument value.
     
     For any two instances `x` and `y` of a type that 
     implements `Multiplicable`, `x.times(y)` may be 
     written as:
     
         x * y"
    shared formal Other times(Other other);
    
}
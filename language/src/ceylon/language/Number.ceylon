/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Abstraction of [[numeric|Numeric]] types with a natural 
 [[total order|Comparable]], including the built-in numeric 
 types [[Integer]] and [[Float]].
 
 A `Number` has a well-defined [[magnitude]] together with a 
 [[sign]] of type [[Integer]], defined for any instance `x`
 as follows:
 
 - if `x==0` then `x.sign==0` and `x.magnitude==0`,
 - if `x>0` then `x.sign==1` and `x.magnitude==x`, or
 - if `x<0` then `x.sign==-1` and `x.magnitude==-x`,
 
 where `0` is the additive identity of the numeric type.
 
 Not every value commonly considered to be a \"number\" is
 a `Number`. For example, complex numbers aren't `Number`s
 since they don't have a total order."
see (class Integer, class Float)
by ("Gavin")
tagged("Numbers")
shared interface Number<Other> of Other
        satisfies Numeric<Other> 
                & Comparable<Other>
        given Other satisfies Number<Other> {
    
    "The magnitude of this number, defined for any instance 
     `x` as:
     
     - `-x` if `x<0`, or 
     - `x` otherwise,
     
     where `0` is the additive identity. Hence:
     
     - `x.magnitude >= 0` for any `x`, and
     - `x.magnitude == 0` if and only if `x==0`."
    aliased("absolute")
    shared default Other magnitude
            => negative 
            then negated 
            else this of Other;
    
    "The sign of this number: 
     
     - `1` if the number is [[positive]], 
     - `-1` if it is [[negative]], or 
     - `0` if it is the additive identity.
     
     Must satisfy:
     
         x.magnitude.timesInteger(x.sign) == x"
    shared default Integer sign 
            => if (positive) then 1
          else if (negative) then -1
          else 0;
    
    "Determine if the number is strictly positive, that is, 
     if `this>0`, where `0` is the additive identity."
    shared formal Boolean positive;
    
    "Determine if the number is strictly negative, that is, 
     if `this<0`, where `0` is the additive identity."
    shared formal Boolean negative;
    
    "The fractional part of this number, after truncation of 
     the integral part. For [[Integral]] numeric types, the 
     fractional part is always zero."
    shared formal Other fractionalPart;
    
    "The integral value of the number after truncation of 
     the fractional part. For [[Integral]] numeric types, 
     the integral value of a number is the number itself."
    shared formal Other wholePart;
    
    "The result of multiplying this number by the given 
     [[Integer]]."
    since("1.1.0")
    shared formal Other timesInteger(Integer integer);
    
    "The result of adding this number to the given 
     [[Integer]]."
    since("1.1.0")
    shared formal Other plusInteger(Integer integer);
    
    "The result of raising this number to the given 
     non-negative [[Integer]] power. For a negative power,
     the behavior is implementation-dependent."
    throws (class AssertionError, 
            "if the exponent is a negative power and this is 
             an integral numeric type")
    since("1.1.0")
    shared formal Other powerOfInteger(Integer integer);
    
    "Compares this number with the given number, returning:
     
     - [[larger]], if the [[difference|minus]] `this-other`
       between the numbers is [[positive]],
     - [[smaller]], if the difference `this-other` between 
       the numbers is [[negative]], or
     - [[equal]] otherwise."
    shared actual default Comparison compare(Other other) 
            => let (diff = this - other)
                if (diff.positive) then larger
           else if (diff.negative) then smaller
           else equal;
    
}


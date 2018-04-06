/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Abstraction of numeric types with the usual binary 
 operations:
 
 - addition, `x + y`, 
 - subtraction, `x - y`, 
 - multiplication, `x * y`, and 
 - division, `x / y`, along with 
 - additive inverse `-x`.
 
 A concrete class which implements this interface should be
 a mathematical _ring_. That is:
 
 - both addition, `+`, and multiplication, `*`, should be
   associative and commutative,
 - there should be additive and multiplicative identities,
   denoted `0` and `1` respectively, satisfying `x+0 == x`
   and `x*1 == x`,
 - every instance `x` should have an additive inverse `-x`, 
   satisfying `x + -x == 0`, and
 - multiplication should distribute over addition, 
   satisfying `x*(y+z) == x*y + x*z`.
 
 It is preferred, but not required, that the class be a
 mathematical _field_. That is, in addition to the above:
 
 - every instance `x` such that `x!=0` should have a 
   multiplicative inverse `1/x`, satisfying `x * 1/x == 1`. 
 
 For numeric types which are not fields, for example, 
 [[Integer]], there is still a division operation, which is
 understood to produce a [[remainder|Integral.remainder]].
 The division operation should satisfy:
 
 - `x*y / y == x`
 
 for any instance `y` other than `0`.
 
 For numeric types which _are_ fields, division never
 produces a remainder, and division should additionally 
 satisfy:
 
 - `x/y * y == x`
 
 for any instance `y` other than `0`.
 
 Some numeric types, for example complex numbers, do not 
 have a [[total order|Comparable]]. Numeric types with a 
 total order also satisfy [[Number]]."
see (interface Number)
by ("Gavin")
tagged("Numbers")
shared interface Numeric<Other> of Other
        satisfies Invertible<Other> 
                & Multiplicable<Other>
        given Other satisfies Numeric<Other> {
    
    "The quotient obtained by dividing this number by the 
     given number. For [[integral|Integral]] numeric types, 
     this operation rounds toward `0`, the additive identity,
     and results in a [[remainder|Integral.remainder]].
     
     When the given [[divisor|other]] is exactly `0`, the 
     additive identity, the behavior depends on the numeric 
     type:
     
     - For some numeric types, including [[Integer]], 
       division by `0` results in an exception.
     - For others, including [[Float]], it results in a 
       special value of the type, for example, [[infinity]]."
    see (function Integral.remainder, 
         value infinity)
    shared formal Other divided(Other other);
    
}

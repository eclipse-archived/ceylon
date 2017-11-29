/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Abstraction of [[additive|Summable]] numeric types which 
 support a unary operation `-x` producing the additive
 inverse of `x`. Every `Invertible` type supports a binary 
 subtraction operation `x-y`.
 
     Integer negativeOne = -1;
     Float delta = x-y;
 
 A concrete class that implements this interface should be a 
 mathematical _group_. That is, it should have an additive 
 identity, denoted `0`, and satisfy:
 
 - `0+x == x+0 == x`
 - `x + -x == 0`
 
 Subtraction must be defined so that it is consistent with
 the additive inverse:
 
 - `x - y == x + -y`"
see (class Integer, class Float)
by ("Gavin")
tagged("Numbers")
shared interface Invertible<Other> of Other
        satisfies Summable<Other>
    given Other satisfies Invertible<Other> {
    
    "The additive inverse of this value."
    since("1.1.0")
    shared formal Other negated;
    
    "The difference between this number and the given 
     number. Must produce the value `x + -y`."
    since("1.1.0")
    shared default Other minus(Other other) => this + -other;
    
}
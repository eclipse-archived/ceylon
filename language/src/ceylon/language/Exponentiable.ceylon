/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Abstraction of [[numeric types|Numeric]] that may be raised 
 to a power using the _exponentiation_ operator `x ^ n` 
 which accepts an instance of `Exponentiable` as its first
 operand, and an exponent as its second operand.
 
     function exp(Float x) => e^x;
 
 The exponentiation operation should obey the usual index
 laws, including:
 
 - `x^0 == 1`
 - `x^1 == x`
 - `x^(-1) == 1/x`
 - `x^(m+n) == x^m * x^n`
 - `x^(m-n) == x^m / x^n`
 - `x^(m*n) == (x^m)^n`
 - `(x*y)^n == x^n * y^n`
 
 where `0` is the additive identity, and `1` is the 
 multiplicative identity.
 
 Note that in general, the type of the exponent may be 
 different to the numeric type which is exponentiated. For
 example, a `Rational` number class might be a subtype of
 `Exponentiable<Rational,Integer>`, thus accepting only
 whole-number exponents."
see (class Integer, class Float)
tagged("Numbers")
shared interface Exponentiable<This,Other> of This
        satisfies Numeric<This>
        given This satisfies Exponentiable<This,Other> 
        given Other satisfies Numeric<Other> {

    "The result of raising this number to the given power."
    shared formal This power(Other other);
    
} 
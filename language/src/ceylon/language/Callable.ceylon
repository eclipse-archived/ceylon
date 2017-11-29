/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A reference to a function. The type arguments encode the 
 [[return type|Return]] of the function along with its 
 [[parameter types|Arguments]]. The parameter types are 
 represented by a tuple type. Functions declared `void` are 
 considered to have the return type `Anything`.
 
 For example, the type of the anonymous function
 `(Float x, Integer y) => x^y+1` is:
 
     Callable<Float, [Float,Integer]>
 
 which we usually abbreviate to `Float(Float,Integer)`.
 
 Likewise, the type of the function reference `plus<Float>` 
 to the function [[plus]] is:
 
     Callable<Float, [Float,Float]>
 
 which we abbreviate as `Float(Float,Float)`.
 
 A variadic function is represented using an unterminated 
 tuple type. For example, the type of the function reference
 `concatenate<Object>` to the function [[concatenate]] is:
 
     Callable<Object[], [{Object*}*]>
 
 which we usually abbreviate `Object({Object*}*)`.
 
 A function with defaulted parameters is represented using
 a union type. For example, the type of the method reference
 `process.writeLine` to the method [[process.writeLine]] is:
 
     Callable<Anything, [String]|[]>
 
 which we usually abbreviate `Anything(String=)`.
 
 Finally, any type of form `Callable<X,Y>` may be 
 abbreviated to `X(*Y)`.
 
 Any instance of `Callable` may be _invoked_ by supplying a 
 positional argument list:
 
     Float(Float,Float) add = plus<Float>;
     value four = add(2.0, 2.0);
 
 or by supplying a tuple containing the arguments:
 
     Float(Float,Float) add = plus<Float>;
     [Float,Float] twoAndTwo = [2.0, 2.0];
     value four = add(*twoAndTwo);
 
 The type of the tuple must be assignable to the type 
 argument of `Arguments`.
 
 There is no reasonable and computationally decidable 
 definition of [[value equality|Object.equals]] for a 
 function reference. Therefore, the `equals()` method of an
 instance of `Callable` always returns `false`, and `x==y`
 always evaluates to `false` for any two function references
 `x` and `y`.
 
 This interface may not be implemented by user-written code."
see (class Tuple)
tagged("Functions")
shared native interface Callable<out Return, in Arguments> 
        given Arguments satisfies Anything[] {}

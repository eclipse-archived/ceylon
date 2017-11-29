/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Given a function with two parameter lists, return a 
 function with the order of the argument lists reversed. The 
 parameter lists may have any number of parameters.
 
 That is, if `fun` has type `W(A,B)(X,Y,Z)` then 
 `shuffle(fun)` has type `W(X,Y,Z)(A,B)`.
 
 This function is often used in conjunction with `curry()`."
see (function curry)
tagged("Functions")
shared Result(*FirstArgs)(*SecondArgs)
shuffle<Result,FirstArgs,SecondArgs>(
            Result(*SecondArgs)(*FirstArgs) f)
        given FirstArgs satisfies Anything[]
        given SecondArgs satisfies Anything[]
            => flatten((SecondArgs secondArgs) 
                => flatten((FirstArgs firstArgs)
                    => unflatten(unflatten(f)(firstArgs))(secondArgs)));
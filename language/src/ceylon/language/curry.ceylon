/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Curries a function, returning a function with two parameter 
 lists, given a function with at least one parameter. The 
 first parameter list of the returned function has just the 
 first parameter of the original function, and the second 
 parameter list has the remaining parameters.
 
 That is, if `fun` has type `W(X,Y,Z)` then 
 `curry(fun)` has type `W(Y,Z)(X)`."
see (function uncurry, function compose)
tagged("Functions")
shared Return(*Rest) curry<Return,Argument,First,Rest>
            (Return(*Tuple<Argument,First,Rest>) f)
            (First first)
        given First satisfies Argument 
        given Rest satisfies Argument[] 
                => flatten((Rest args) 
                        => unflatten(f)(Tuple(first, args)));

"Uncurries a function, returning a function with one 
 parameter list, given a function with two parameter lists, 
 where the first parameter list has exactly one parameter. 
 The parameter list of the returned function has the 
 parameter of the first parameter list of the original
 function, followed by all parameters of the second 
 parameter list.
 
 That is, if `fun` has type `W(Y,Z)(X)` then `uncurry(fun)` 
 has type `W(X,Y,Z)`."
see (function curry, function compose)
tagged("Functions")
shared Return(*Tuple<Argument,First,Rest>) 
        uncurry<Return,Argument,First,Rest>
            (Return(*Rest) f(First first))
        given First satisfies Argument 
        given Rest satisfies Argument[] 
                => flatten((Tuple<Argument,First,Rest> args) 
                        => unflatten(f(args.first))(args.rest));

/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Given a function with return type `Y`, and a second 
 function with a single parameter also of type `Y`, return 
 the composition of the two functions. The first function 
 may have any number of parameters.
 
 For any such functions `f()` and `g()`,
 
     compose(g,f)(*args)==g(f(*args))
 
 for every possible argument tuple `args` of `f()`."
see(function curry, function uncurry)
tagged("Functions")
shared X(*Args) compose<X,Y,Args>(X(Y) x, Y(*Args) y) 
        given Args satisfies Anything[]
               => flatten((Args args) => x(y(*args)));
               //=> flatten((Args args) => x(unflatten(y)(args)));

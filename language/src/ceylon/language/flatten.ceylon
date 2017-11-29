/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Given a function with a single parameter of tuple type 
 `[P1, P2, ..., Pn]`, return a function with multiple 
 parameters of type `P1`, `P2`, ..., `Pn`.
 
 That is, if `fun` has type `W([X,Y,Z])` then `flatten(fun)` 
 has type `W(X,Y,Z)`.
 
 In the case of a function whose parameter type is a 
 sequence type or unterminated tuple type, the returned 
 function is variadic:
 
 - if the given function accepts `[S*]`, the returned 
   function has a single variadic parameter of type `S*`,
 - if the given function accepts `[S+]`, the returned 
   function has a single variadic parameter of type `S+`,
 - if the given function accepts `[P1, P2, ..., Pn, S*]`, 
   the returned function has multiple parameters with types
   `P1`, `P2`, ..., `Pn`, `S*`, or
 - if the given function accepts `[P1, P2, ..., Pn, S+]`,
   the returned function has multiple parameters with types
   `P1`, `P2`, ..., `Pn`, `S+`."
see(function unflatten)
tagged("Functions")
shared native Return(*Args) flatten<Return,Args>
        (Return tupleFunction(Args tuple))
        given Args satisfies Anything[];

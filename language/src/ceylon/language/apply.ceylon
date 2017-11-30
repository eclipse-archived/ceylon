/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Applies an arbitrary [[Callable]] to the given 
 [[arguments|args]]. The arguments must be packaged into a 
 [[Tuple]] whose type is compatible with the `Callable` type. 
 
 For example, given the following argument tuple:
 
      [Boolean(Character), Boolean, Boolean] tuple
          = [Character.whitespace, true, false];
 
 We can apply [[String.split]] to the arguments given in
 `tuple` as follows:
 
     String string = ... ;
     {String*} strings = apply(string.split, tuple);
 
 Application may be abbreviated using the spread operator:
 
     String string = ... ;
     {String*} strings = string.split(*tuple)
 
 In practice, this behaves as if the `Callable` were called 
 with the elements of the tuple as its arguments. The
 examples above are both equivalent to:
 
     string.split(Character.whitespace, true, false)"
see (function unflatten)
tagged("Functions")
shared Return apply<Return,Args>
            (Callable<Return,Args> f, Args args)
        given Args satisfies Anything[]
        => f(*args);

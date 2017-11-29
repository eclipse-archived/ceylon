/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Abstract supertype of resources which are created at the 
 beginning of a `try` statement and destroyed when the 
 statement completes. Unlike an [[Obtainable]] resource, a 
 single instance of `Destroyable` may not be reused between 
 multiple `try` statements or multiple executions of the 
 same `try` statement. 
 
     try (tx = Transaction()) {
         ...
     }
 
 - The resource is instantiated before the body of the `try` 
   statement is executed, and
 - [[destroy]] is called when execution of the body of the 
   `try` statement ends, even if an exception propagates out 
   of the body of the `try`."
see (interface Obtainable)
tagged("Basic types")
since("1.1.0")
shared interface Destroyable satisfies Usable {
    
    "Destroy this resource. Called when execution of the 
     body of the `try` statement ends, even if an exception 
     propagates out of the body of the `try`."
    shared formal void destroy(
        "The exception propagating out of the body of the 
         `try` statement, or `null` if no exception was
         propagated."
        Throwable? error);
    
}
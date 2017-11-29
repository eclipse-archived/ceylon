/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"""Represents classes or functions that you can apply in a type-unsafe way.
   """
since("1.1.0")
shared sealed interface Applicable<out Type=Anything, in Arguments=Nothing> 
        satisfies Callable<Type,Arguments>
        given Arguments satisfies Anything[] {
    
    "Type-unsafe application, to be used when the argument types are unknown until runtime.
     
     This has the same behaviour as invoking the applicable directly, but exchanges compile-time type
     safety with runtime checks."
    throws(class IncompatibleTypeException, "If any argument is not assignable to this applicable's corresponding parameter")
    throws(class InvocationException, "If there are not enough or too many provided arguments")
    shared formal Type apply(Anything* arguments);
    
    "Type-unsafe application by name, to be used when the argument types are unknown until runtime.
     
     This has the same behaviour as invoking the applicable directly, but exchanges compile-time type
     safety with runtime checks."
    throws(class IncompatibleTypeException, "If any argument is not assignable to this applicable's corresponding parameter")
    throws(class InvocationException, "If there are not enough or too many provided arguments, 
                                         or if the target does not support named invocation")
    shared formal Type namedApply({<String->Anything>*} arguments);
}

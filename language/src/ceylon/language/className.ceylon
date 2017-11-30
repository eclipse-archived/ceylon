/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta {
    type
}

"""Return the name of the concrete class of the given object, 
   in a format native to the virtual machine. For example,
   `className(0)` evaluates to:
   
   - `"ceylon.language.Integer"` on the Java Virtual Machine, 
     and to
   - `"ceylon.language::Integer"` on a JavaScript VM.
   
   To obtain a platform-independent class name, use the 
   [[ceylon.language.meta::type]] function to obtain a
   metamodel object, for example:
   
       type(1).declaration.qualifiedName
   
   evaluates to `"ceylon.language::Integer"` on every 
   platform."""
tagged("Metamodel")
see (function type)
shared native String className(Anything obj);
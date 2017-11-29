/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"""The type of the [[null]] value. Any union type of form 
   `Null|T` is considered an _optional_ type, whose values
   include `null`. Any type of this form may be written as
   `T?` for convenience.
   
   The `if (exists ... )` construct, or, alternatively,
   `assert (exists ...)`, may be used to narrow an optional 
   type to a _definite_ type, that is, a subtype of 
   [[Object]]:
   
       String? firstArg = process.arguments.first;
       if (exists firstArg) {
           print("hello " + firstArg);
       }
   
   The `else` operator evaluates its second operand if and 
   only if its first operand is `null`:
   
       String name = process.arguments.first else "world";
   
   The `then` operator evaluates its second operand when
   its first operand evaluates to `true`, and produces `null` 
   otherwise:
   
       Float? diff = x>=y then x-y;
   
   The `?.` operator may be used to evaluate an attribute
   or invoke a method of an optional type, evaluating to
   `null` when the receiver is missing:
   
       value [firstName, lastName] =
               let (fullName = process.arguments.first?.trimmed,
                    bits = fullName?.split()?.sequence() else []) 
                       [bits[0], bits[1]];
       assert (exists firstName, exists lastName);
   
   No equivalence relation is defined for `Null`. In 
   particular, neither `null==null` nor `null===null` are
   considered meaningful. Therefore, `Null` is neither
   [[Identifiable]], nor does it define 
   [[value equality|Object.equals]]."""
see (value null)
by ("Gavin") 
tagged("Basic types")
shared abstract class Null() 
        of null
        extends Anything() {}

"The null value."
by ("Gavin")
tagged("Basic types")
shared object null extends Null() {}

/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language { printTrace=printStackTrace }

"The abstract supertype of values indicating exceptional 
 conditions. An exception may be raised using the `throw` 
 statement, and handled using the `catch` clause of the `try` 
 statement. An instance of `Throwable` may be passed from
 `throw` to `catch`.
     
     void tryToDoIt() {
         if (canDoIt()) {
             doIt();
         }
         else {
             throw CantDoIt(); //the Throwable
         }
     }
     
     try {
         tryToDoIt();
     }
     catch (CantDoIt e) {
         e.printStackTrace();
     }
 
 An instance of `Throwable` represents a problem, typically 
 an _unexpected failure_. Either:
 
 - a unrecoverable error in the program, especially an 
   [[AssertionError]], or
 - a transient, and possibly-recoverable [[Exception]].
 
 The use of the exceptions facility to manage _expected 
 failures_, that is, failures that are usually handled by 
 the immediate caller of an operation, is discouraged. 
 Instead, the failure should be represented as a return 
 value of the operation being called.
 
 For example, nonexistence of a file should not result in an 
 exception. Instead, an `openFile()` operation should return 
 the type `File?`, where a `null` return value indicates 
 nonexistence. On the other hand, failure to read from an
 already open file could result in an `Exception`."
see (class Exception, class AssertionError)
by ("Gavin", "Tom")
tagged("Basic types")
since("1.1.0")
shared abstract sealed native 
class Throwable(description=null, cause=null) {
    
    "The underlying cause of this exception."
    shared Throwable? cause;
    
    "A description of the problem."
    String? description;
    
    "A message describing the problem. This default 
     implementation returns the description, if any, or 
     otherwise the message of the cause, if any."
    see (value cause)
    shared default String message 
            => description else cause?.message else "";
    
    shared actual default String string 
            => className(this) + " \"``message``\"";
    
    "Print the stack trace to the standard error of the 
     virtual machine process."
    see (function printTrace)
    shared void printStackTrace() => printTrace(this);
    
    "The given exception was suppressed in order to 
     propagate this exception."
    shared native void addSuppressed(Throwable suppressed);
    
    "The exceptions that were suppressed in order to 
     propagate this exception."
    shared native Throwable[] suppressed;
}

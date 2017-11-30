/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"An error that occurs due to an incorrectly written program. 
 An instance is thrown when an assertion fails, that is, 
 when a condition in an `assert` statement evaluates to 
 false at runtime.
 
 The assertion
 
     \"x must be positive\"
     assert (x>0);
 
 has almost the same effect as this `if` statement
     
     if (!x>0) {
         throw AssertionError(\"x must be positive\");
     }"
shared native class AssertionError(message, Throwable? cause=null)
        extends Throwable(message, cause) {
    "A message describing the assertion that failed. In the
     case of an `assert` statement, it is the text specified
     by the `doc` annotation.
     
     Certain tools interpret this message as 
     markdown-formatted text."
    String message;
}
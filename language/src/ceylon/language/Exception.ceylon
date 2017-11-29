/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"The supertype of all unexpected transient failures. An
 `Exception` is usually unrecoverable from the point of view 
 of the immediate caller of an operation.
 
 For example, code that interacts with a relational database 
 isn't typically written to recover from transaction 
 rollback or loss of network connectivity. However, these 
 conditions aren't completely unrecoverable from the point 
 of view of the program's generic exception handling 
 infrastructure. Therefore, they are best treated as 
 `Exception`s.
 
 A subclass of `Exception` represents a more specific kind 
 of problem, and may define additional attributes which 
 propagate information about problems of that kind."
by ("Gavin", "Tom")
shared native class Exception(description=null, cause=null) 
        extends Throwable(description, cause) {
    
    "A description of the problem."
    String? description;
    
    "The underlying cause of this exception."
    Throwable? cause;
    
}
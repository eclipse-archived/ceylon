/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"""An abstraction of things that have a value which be [[got|get]], 
   and possibley set.  
   """
since("1.2.0")
shared sealed interface Gettable<out Get=Anything, in Set=Nothing> {
    
    "Reads the current value for this value binding. Note that in the case of getter
     values, this can throw if the getter throws."
    throws(class StorageException,
        "If this attribute is not stored at runtime, for example if it is neither shared nor captured.")
    shared formal Get get();
    
    "Changes this variable's value to the given new value. Note that in the case of
     setter attributes, this can throw if the setter throws."
    throws(class StorageException,
        "If this attribute is not stored at runtime, for example if it is neither shared nor captured.")
    shared formal void set(Set newValue);
    
    "Non type-safe equivalent to [[Value.set]], to be used when you don't know the 
     variable type at compile-time. This only works if the underlying value is 
     variable. Note that if the underlying variable is a setter, this can throw 
     exceptions thrown in the setter block."
    throws(class IncompatibleTypeException, 
        "If the specified new value is not of a subtype of this variable's type")
    throws(class MutationException, 
            "If this value is not variable")
    throws(class StorageException,
                "If this attribute is not stored at runtime, for example if it is neither shared nor captured.")
    shared formal void setIfAssignable(Anything newValue);
}
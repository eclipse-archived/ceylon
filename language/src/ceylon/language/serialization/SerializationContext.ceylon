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
    classDeclaration, type
}

"A context representing serialization of many objects to a 
 single output stream. 
 
 The serialization library obtains an instance by calling 
 [[serialization]] and then uses
 [[references]] to traverse the instances reachable from the 
 instance(s) being serialized.
 
 It is the serialization library's responsibility to 
 manage object identity and handle cycles in the graph 
 of object references. For example a serialization library 
 that produced a hierarchical format might ignore identity 
 when an instance is encountered multiple times 
 (resulting in duplicate subtrees in the output), and 
 simply throw an exception if it encountered a cycle. 
 "
shared sealed
interface SerializationContext {
    // could be generic
    "Obtain the references of the given instance."
    throws(class SerializationException,
        "If the class is not serializable")
    shared formal References references(Anything instance);
}

class SerializationContextImpl() satisfies SerializationContext {
    shared actual References references(Anything instance) {
        if (classDeclaration(instance).serializable) {
            return ReferencesImpl(instance);
        } else {
            throw SerializationException("instance of non-serializable class: ``type(instance)``");
        }
    }
}

/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Return the system-defined identity hash value of the given 
 [[value|identifiable]]. This hash value is consistent with 
 [[identity equality|Identifiable.equals]]."
see (function identical)
shared native Integer identityHash(Identifiable identifiable);

shared native("jvm") 
Integer identityHash(Identifiable identifiable) {
    import java.lang {
        System
    }

    return System.identityHashCode(identifiable);
}

native("js")
variable Integer _BasicId = 0;

shared native("js") 
Integer identityHash(Identifiable identifiable) {
    dynamic {
        dynamic x = identifiable;
        if (exists hash = x._BasicId) {
            return hash;
        }
        else {
            Integer hash = _BasicId++;
            x._BasicId = hash;
            return hash;
        }
    }
}
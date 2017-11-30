/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.serialization {
    Outer
}

"Implementation of [[Outer]], in ceylon.language.impl because although 
 compiled user classes depend on it, it is not part of the public API."
shared object outerImpl satisfies Outer {
    "The outer instance of the given member [[instance]]."
    shared actual Object referred(/*<Instance>*/Object instance) {
        return reach.getObject(instance, this);
    }
    
    shared actual String string => "Outer";
}


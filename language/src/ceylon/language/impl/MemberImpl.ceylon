/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.declaration {
    ValueDeclaration, FunctionOrValueDeclaration
}
import ceylon.language.serialization {
    Member, UninitializedLateValue
}

"Implementation of [[Member]], in ceylon.language.impl because although 
 compiled user classes depend on it, it is not part of the public API."
shared class MemberImpl satisfies Member {
    shared actual ValueDeclaration attribute;
    
    shared new (FunctionOrValueDeclaration attribute) {
        assert(is ValueDeclaration attribute);
        this.attribute = attribute;
    }
    
    shared actual Anything|UninitializedLateValue referred(Object/*<Instance>*/ instance) {
        return reach.getAnything(instance, this);
    }
    
    shared actual String string
            => "Member [attribute=``attribute``]";
    
    shared actual Integer hash => attribute.hash;
    shared actual Boolean equals(Object other) {
        if (is MemberImpl other) {
            return this === other || attribute == other.attribute;
        } else {
            return false;
        }
    }
}


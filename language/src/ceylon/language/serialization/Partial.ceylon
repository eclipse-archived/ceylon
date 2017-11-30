/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.model {
    ClassModel
}

"A partially reconstructed instance: 
 Holds state for reconstructing an instance which is built up 
 progressively by calls to the [[DeserializationContext]]."
abstract class Partial(id) {
    
    "The id"// It's an object rather than an id to save space: we require a lot of partials!
    shared Object id;
    
    "The class, if we know it yet"
    shared variable ClassModel<>? clazz = null;
    
    "The containing instance (a partial for it, or the instance itself).
     null if this partial is not a member or we don't know the container yet."
    shared variable Anything container = null;
    
    "The (partially initialized) instance, if it has been [[instantiated|instantiate]], or null."
    shared variable Anything instance_ = null;
    
    "The state, mapping references to the 
     **id** of the corresponding value in the [[DeserializationContext]].
     nullified by a successful call to [[initialize]]."
    //The Value type is not Id to save space
    shared variable NativeMap<ReachableReference, Object>? state = NativeMap<ReachableReference, Object>();
    
    "Add some state."
    shared void addState(ReachableReference attrOrIndex, Object partialOrComplete) {
        assert(exists s=state);
        s.put(attrOrIndex, partialOrComplete);
    }
    //shared formal void addState(String|Integer attrOrIndex, Object partialOrComplete);
    
    "Creates (but does not initialize) the [[instance_]] using backend-specific reflection.
     After normal completion of this method instance_ will be non-null."
    throws(class DeserializationException,
        "* the class of the instance has already been specified
         * instance is a member instance and the container has not been specified")
    shared formal void instantiate();
    
    "Initializes the [[instance_]] using backend-specific reflection.
     After normal completion of this method state will be null."
    throws(class DeserializationException,
        "the partial contains insufficient state")
    shared formal void initialize<Id>(DeserializationContextImpl<Id> context)
            given Id satisfies Object;
    
    "Whether the partial has been instantiated"
    shared Boolean instantiated => instance_ exists;
    "Whether the partial has been initialized"
    shared Boolean initialized => !state exists;
    "Whether the partial is for an instance of a member class"
    shared Boolean member => container exists;
    
    "Gets the fully initialized instance, or throws"
    shared Anything instance() {
        assert(instantiated && initialized);
        return instance_;
    }
    
    "The ids of the instances that this instance refers to"
    shared {Object*} refersTo {
        if (exists s=state) {
            return s.items;
        } else {
            return [];
        }
    }
}

/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.impl{
    BaseIterator, 
    BaseIterable,
    reach
}

class ReferencesImpl(instance) extends BaseIterable<ReachableReference->Anything, Null>() 
        satisfies References&Identifiable {
    shared actual Anything instance;
    
    shared actual Iterator<ReachableReference->Anything> iterator() {
        if (exists instance) {
            return object extends BaseIterator<ReachableReference->Anything>() 
                    satisfies Identifiable {
                Iterator<ReachableReference> it = outer.references.iterator();
                
                shared actual <ReachableReference->Anything>|Finished next() {
                    ReachableReference|Finished next = it.next();
                    if (is Finished next) {
                        return finished;
                    } else {
                        return next->next.referred(instance);
                    }
                }
            };
        } else {
            return emptyIterator;
        }
    }
    
    shared actual Iterable<ReachableReference/*<Instance>*/> references {
        return object extends BaseIterable<ReachableReference, Null>() 
                satisfies Identifiable {
            shared actual Iterator<ReachableReference> iterator() {
                return reach.references(instance);
            }
        };
    }
}

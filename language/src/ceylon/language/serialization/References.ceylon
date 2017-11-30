/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Exposes the instances directly reachable from a given instance."
shared sealed interface References/*<Instance>*/
    // could be generic 
        satisfies {<ReachableReference/*<Instance>*/->Anything>*} {
    
    "The instance"
    shared formal Anything/*<Instance>*/ instance;
    
    "The references that are reachable from the [[instance]]."
    shared formal Iterable<ReachableReference/*<Instance>*/> references;
}


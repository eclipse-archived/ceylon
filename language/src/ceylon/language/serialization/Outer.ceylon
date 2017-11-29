/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A member instance referring to its outer instance."
shared sealed interface Outer /*<Instance>*/
        satisfies ReachableReference/*<Instance>*/ {
    "The outer instance of the given member [[instance]]."
    shared actual formal Object referred(/*<Instance>*/Object instance);
}
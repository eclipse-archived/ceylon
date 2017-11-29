/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A means via which one instance can refer to another."
shared interface ReachableReference/*<Instance>*/ // Reachable
        of Member|Element|Outer {
    "The [[referred]] instance reachable from the given [[instance]]."
    shared formal Anything referred(Object/*<Instance>*/ instance);
}



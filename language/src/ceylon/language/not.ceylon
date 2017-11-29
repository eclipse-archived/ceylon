/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Returns a function which is the logical negation of the 
 given predicate function."
tagged("Functions")
since("1.1.0")
shared Boolean not<in Value>(
    "The predicate function to negate"
    Boolean(Value) p)(Value val) 
        => !p(val);
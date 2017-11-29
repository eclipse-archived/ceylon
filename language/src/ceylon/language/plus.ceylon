/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Add the given [[Summable]] values.
 
     (1..100).by(2).fold(0)(plus<Integer>)"
see (function times, function sum)
tagged("Numbers")
shared Value plus<Value>(Value x, Value y)
        given Value satisfies Summable<Value>
        => x+y;
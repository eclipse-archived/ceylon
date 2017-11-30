/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Multiply the given [[Numeric]] values.
 
     (1..100).by(2).fold(1)(times<Integer>)"
see (function plus, function product)
tagged("Numbers")
shared Value times<Value>(Value x, Value y)
        given Value satisfies Numeric<Value>
        => x*y;
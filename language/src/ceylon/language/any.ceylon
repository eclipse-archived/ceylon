/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Determines if any one of the given boolean values 
 (usually a comprehension) is `true`.
 
     Boolean anyNegative = any { for (x in xs) x<0.0 };
 
 If there are no boolean values, return `false`."
see (function every, 
     function Iterable.any)
tagged("Streams")
shared Boolean any({Boolean*} values) {
    for (val in values) {
        if (val) {
            return true;
        }
    }
    return false;
}

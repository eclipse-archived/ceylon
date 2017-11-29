/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Determines if every one of the given boolean values 
 (usually a comprehension) is `true`.
 
     Boolean allPositive = every { for (x in xs) x>0.0 };
 
 If there are no boolean values, return `true`."
see (function any, 
     function Iterable.every)
tagged("Streams")
shared Boolean every({Boolean*} values) {
    for (val in values) {
        if (!val) {
            return false;
        }
    }
    return true;
}

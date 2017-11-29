/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A count of the number of `true` items in the given values.
 
     Integer negatives = count { for (x in xs) x<0.0 };"
see (function Iterable.count)
tagged("Streams")
shared Integer count({Boolean*} values) {
    variable value count=0;
    for (val in values) {
        if (val) {
            count++;
        }
    }
    return count;
}

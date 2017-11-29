/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A comparator for [[Entry]]s which compares their keys 
 according to the given [[comparing]] function.
 
     value sortedEntries = map.sort(byKey(byIncreasing(String.lowercased)));
 
 This function is intended for use with [[Iterable.sort]]
 and [[Iterable.max]]."
see (function byItem)
tagged("Comparisons")
shared Comparison byKey<Key>
        (Comparison comparing(Key x, Key y))
            (Key->Object x, Key->Object y) 
        given Key satisfies Object =>
                comparing(x.key, y.key);
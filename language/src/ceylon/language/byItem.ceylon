/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A comparator for [[Entry]]s which compares their items 
 according to the given [[comparing]] function.
 
     value sortedEntries = map.sort(byItem(byIncreasing(String.lowercased)));
 
 This function is intended for use with [[Iterable.sort]]
 and [[Iterable.max]]."
see (function byKey)
tagged("Comparisons")
shared Comparison byItem<Item>
        (Comparison comparing(Item x, Item y))
            (Object->Item x, Object->Item y) 
        given Item satisfies Object => 
                comparing(x.item, y.item);
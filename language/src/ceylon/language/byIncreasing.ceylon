/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Produces a comparator function which orders elements in 
 increasing order according to the [[Comparable]] value 
 returned by the given [[comparable]] function.
 
      \"Hello World!\".sort(byIncreasing(Character.lowercased))
 
 This function is intended for use with [[Iterable.sort]]
 and [[Iterable.max]]."
see (function byDecreasing,
     function increasing,
     function Iterable.max,
     function Iterable.sort)
tagged("Functions", "Comparisons")
shared Comparison byIncreasing<Element,Value>
        (Value comparable(Element e))
            (Element x, Element y)
        given Value satisfies Comparable<Value> 
                => comparable(x)<=>comparable(y);

"A comparator function which orders elements in increasing 
 [[natural order|Comparable]].
 
        \"Hello World!\".sort(increasing)
 
 This function is intended for use with [[Iterable.sort]]
 and [[Iterable.max]]."
see (function decreasing,
     function byIncreasing,
     function Iterable.max,
     function Iterable.sort)
tagged("Comparisons")
since("1.2.0")
shared Comparison increasing<Element>(Element x, Element y)
        given Element satisfies Comparable<Element> 
        => x<=>y;

"A comparator function which orders [[entries|Entry]] by 
 increasing [[natural order|Comparable]] of their 
 [[keys|Entry.key]].
        
 This function is intended for use with [[Iterable.sort]]
 and [[Iterable.max]]."
tagged("Comparisons")
since("1.2.0")
shared Comparison increasingKey<Key>
        (Key->Anything x, Key->Anything y)
        given Key satisfies Comparable<Key>
        => x.key <=> y.key;

"A comparator function which orders [[entries|Entry]] by 
 increasing [[natural order|Comparable]] of their 
 [[items|Entry.item]].
        
 This function is intended for use with [[Iterable.sort]]
 and [[Iterable.max]]."
tagged("Comparisons")
since("1.2.0")
shared Comparison increasingItem<Item>
        (Object->Item x, Object->Item y)
        given Item satisfies Comparable<Item>
        => x.item <=> y.item;


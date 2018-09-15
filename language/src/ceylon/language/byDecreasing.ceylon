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
 decreasing order according to the [[Comparable]] value 
 returned by the given [[comparable]] function.
 
      \"Hello World!\".sort(byDecreasing(Character.lowercased))
 
 This function is intended for use with [[Iterable.sort]]
 and [[Iterable.max]]."
see (function byIncreasing,
     function decreasing,
     function Iterable.max,
     function Iterable.sort)
tagged("Functions", "Comparisons")
shared Comparison byDecreasing<Element,Value>
        (Value comparable(Element e))
            (Element x, Element y)
        given Value satisfies Comparable<Value>
                => comparable(y)<=>comparable(x);

"A comparator function which orders elements in decreasing 
 [[natural order|Comparable]].
 
        \"Hello World!\".sort(decreasing)
 
 This function is intended for use with [[Iterable.sort]]
 and [[Iterable.max]]."
see (function increasing,
     function byDecreasing,
     function Iterable.max,
     function Iterable.sort)
tagged("Comparisons")
since("1.2.0")
shared Comparison decreasing<Element>(Element x, Element y)
        given Element satisfies Comparable<Element> 
        => y<=>x;

"A comparator function which orders [[entries|Entry]] by 
 decreasing [[natural order|Comparable]] of their 
 [[keys|Entry.key]].
        
 This function is intended for use with [[Iterable.sort]]
 and [[Iterable.max]]."
tagged("Comparisons")
since("1.2.0")
shared Comparison decreasingKey<Key>
        (Key->Anything x, Key->Anything y)
        given Key satisfies Comparable<Key>
        => y.key <=> x.key;

"A comparator function which orders [[entries|Entry]] by 
 decreasing [[natural order|Comparable]] of their 
 [[items|Entry.item]].
        
 This function is intended for use with [[Iterable.sort]]
 and [[Iterable.max]]."
tagged("Comparisons")
since("1.2.0")
shared Comparison decreasingItem<Item>
        (Object->Item x, Object->Item y)
        given Item satisfies Comparable<Item>
        => y.item <=> x.item;

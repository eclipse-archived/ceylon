/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Given two streams, form a new stream consisting of all 
 [[entries|Entry]] where, for any given index in the 
 resulting stream, the [[key|Entry.key]] of the entry is the 
 element occurring at the same index in the 
 [[first stream|keys]], and the [[item|Entry.item]] is the 
 element occurring at the same index in the 
 [[second stream|items]]. The length of the resulting stream 
 is the length of the shorter of the two given streams.
 
 Thus:
 
     zipEntries(keys, items)[i] == keys[i] -> items[i]
 
 for every `0<=i<smallest(keys.size,items.size)`."
tagged("Streams")
shared Iterable<<Key->Item>,KeyAbsent|ItemAbsent> 
zipEntries<Key,Item,KeyAbsent,ItemAbsent>
        (Iterable<Key,KeyAbsent> keys, 
         Iterable<Item,ItemAbsent> items)
        given Key satisfies Object
        given KeyAbsent satisfies Null
        given ItemAbsent satisfies Null
        => mapPairs(keys, items, Entry<Key,Item>);

"Given two streams, form a new stream consisting of all 
 pairs where, for any given index in the resulting stream, 
 the first element of the pair is the element occurring at 
 the same index in the [[first stream|firstElements]], and 
 the second element of the pair is the element occurring at 
 the same index in the [[second stream|secondElements]]. The 
 length of the resulting stream is the length of the shorter 
 of the two given streams.
 
 Thus:
 
     zipPairs(xs, ys)[i] == [xs[i], ys[i]]
 
 for every `0<=i<smallest(xs.size,ys.size)`."
tagged("Streams")
shared Iterable<[First,Second],FirstAbsent|SecondAbsent> 
zipPairs<First,Second,FirstAbsent,SecondAbsent>
        (Iterable<First,FirstAbsent> firstElements, 
         Iterable<Second,SecondAbsent> secondElements)
        given FirstAbsent satisfies Null
        given SecondAbsent satisfies Null
        => mapPairs(firstElements, secondElements,
            (First first, Second second) => [first,second]);

"Given a stream of values, and a stream of [[tuples|Tuple]], 
 produce a new stream of tuples formed by prepending the 
 values in the [[first stream|heads]] to the tuples in the 
 [[second stream|tails]]. The length of the resulting stream 
 is the length of the shorter of the two given streams.
 
 Thus:
 
     zip(heads, tails)[i] == [heads[i], *tails[i]]
 
 for every `0<=i<smallest(heads.size,tails.size)`."
tagged("Streams")
shared Iterable<Tuple<Element|Head,Head,Tail>,HeadAbsent|TailAbsent> 
zip<Element,Head,Tail,HeadAbsent,TailAbsent>
        (Iterable<Head,HeadAbsent> heads, 
         Iterable<Tail,TailAbsent> tails)
        given Tail satisfies Element[]
        given HeadAbsent satisfies Null
        given TailAbsent satisfies Null
        => mapPairs(heads, tails, 
            Tuple<Element|Head,Head,Tail>);

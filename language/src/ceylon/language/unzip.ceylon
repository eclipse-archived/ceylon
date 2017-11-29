/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Given a stream of tuples, return two streams. The
 first stream produces the first elements of the
 given tuples, and the second stream produces the
 remaining elements of the given tuples.
 
 Thus:
 
     tuples[i] == [unzip(tuples)[0][i], 
                  *unzip(tuples)[1][i]]"
tagged("Streams")
shared [Iterable<Head,Absent>, Iterable<Tail,Absent>] 
unzip<Element,Head,Tail,Absent>
        (Iterable<Tuple<Element|Head,Head,Tail>,Absent> tuples)
        given Tail satisfies Element[]
        given Absent satisfies Null
        => [tuples.map((Tuple<Element|Head,Head,Tail> tuple) 
                        => tuple.first),
            tuples.map((Tuple<Element|Head,Head,Tail> tuple) 
                        => tuple.rest)];

"Given a stream of pairs, return two streams. The
 first stream produces the first elements of the
 given pairs, and the second stream produces the
 second elements of the given pairs.
 
 Thus:
 
     pairs[i] == [unzipPairs(pairs)[0][i], 
                  unzipPairs(pairs)[1][i]]"
tagged("Streams")
shared [Iterable<First,Absent>, Iterable<Second,Absent>] 
unzipPairs<First,Second,Absent>
        (Iterable<[First,Second],Absent> pairs)
        given Absent satisfies Null
        => [pairs.map(([First,Second] pair) => pair[0]),
            pairs.map(([First,Second] pair) => pair[1])];

"Given a stream of entries, return two streams. The
 first stream produces the keys of the given entries, 
 and the second stream produces the items of the given 
 entries.
 
 Thus:
 
     entries[i] == unzipEntries(entries)[0][i] 
                -> unzipEntries(entries)[1][i]"
tagged("Streams")
shared [Iterable<Key,Absent>, Iterable<Item,Absent>] 
unzipEntries<Key,Item,Absent>
        (Iterable<<Key->Item>,Absent> entries)
        given Key satisfies Object
        given Absent satisfies Null
        => [entries.map(Entry<Key,Item>.key),
            entries.map(Entry<Key,Item>.item)];

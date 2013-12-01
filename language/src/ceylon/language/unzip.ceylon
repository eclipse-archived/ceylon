"Given a stream of tuples, return two streams. The
 first stream produces the first elements of the
 given tuples, and the second stream produces the
 remaining elements of the given tuples.
 
 Thus:
 
     tuples[i]==[unzip(tuples)[0][i],*unzip(tuples)[1][i]]"
shared [{Head*}, {Tail*}] unzip<Element,Head,Tail>
        ({Tuple<Element|Head,Head,Tail>*} tuples)
        given Tail satisfies Element[]
        => [tuples.map((Tuple<Element|Head,Head,Tail> tuple) => tuple.first),
            tuples.map((Tuple<Element|Head,Head,Tail> tuple) => tuple.rest)];

"Given a stream of pairs, return two streams. The
 first stream produces the first elements of the
 given pairs, and the second stream produces the
 second elements of the given pairs.
 
 Thus:
 
     pairs[i]==[unzipPairs(pairs)[0][i],unzipPairs(pairs)[1][i]]"
shared [{First*}, {Second*}] unzipPairs<First,Second>
        ({[First,Second]*} pairs)
        => [pairs.map(([First,Second] pair) => pair[0]),
            pairs.map(([First,Second] pair) => pair[1])];

"Given a stream of entries, return two streams. The
 first stream produces the keys of the given entries, 
 and the second stream produces the items of the given 
 entries.
 
 Thus:
 
     entries[i]==unzipEntries(entries)[0][i]->unzipEntries(entries)[1][i]"
shared [{Key*}, {Item*}] unzipEntries<Key,Item>
        ({<Key->Item>*} entries)
        given Key satisfies Object
        given Item satisfies Object
        => [entries.map(Entry<Key,Item>.key),
            entries.map(Entry<Key,Item>.item)];

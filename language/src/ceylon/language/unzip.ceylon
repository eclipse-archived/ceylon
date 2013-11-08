"Given a stream of tuples, return two streams. The
 first stream produces the first elements of the
 given tuples, and the second stream produces the
 remaining elements of the given tuples.
 
 Thus:
 
     unzip(tuples)[i]==[tuples[i].first,*tuples[i].rest]"
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
 
     unzipPairs(pairs)[i]==[pairs[i][0],pairs[i][1]]"
shared [{First*}, {Second*}] unzipPairs<First,Second>
        ({[First,Second]*} pairs)
        => [pairs.map(([First,Second] pair) => pair[0]),
            pairs.map(([First,Second] pair) => pair[1])];

"Given a stream of entries, return two streams. The
 first stream produces the keys of the given entries, 
 and the second stream produces the items of the given 
 entries.
 
 Thus:
 
     unzipEntries(entries)[i]==[entries[i].key,entries[i].item]"
shared [{Key*}, {Item*}] unzipEntries<Key,Item>
        ({<Key->Item>*} entries)
        given Key satisfies Object
        given Item satisfies Object
        => [entries.map(Entry<Key,Item>.key),
            entries.map(Entry<Key,Item>.item)];

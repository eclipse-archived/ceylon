"Given two streams, form a new stream consisting of
 all entries where, for any given index in the resulting
 stream, the key of the entry is the element occurring 
 at the same index in the first stream, and the item is 
 the element occurring at the same index in the second 
 stream. The length of the resulting stream is the 
 length of the shorter of the two given streams.
 
 Thus:
 
     zipEntries(keys,items)[i]==keys[i]->items[i]
 
 for every `0<=i<min({keys.size,items.size})`."
shared Iterable<<Key->Item>,KeyAbsent|ItemAbsent> zipEntries<Key,Item,KeyAbsent,ItemAbsent>
        (Iterable<Key,KeyAbsent> keys, Iterable<Item,ItemAbsent> items)
        given Key satisfies Object
        given Item satisfies Object
        given KeyAbsent satisfies Null
        given ItemAbsent satisfies Null
        => mapPairs(Entry<Key,Item>, keys, items);

"Given two streams, form a new stream consisting of
 all pairs where, for any given index in the resulting
 stream, the first element of the pair is the element 
 occurring at the same index in the first stream, and 
 the second element of the pair is the element occurring 
 at the same index in the second stream. The length of 
 the resulting stream is the length of the shorter of the 
 two given streams.
 
 Thus:
 
     zipPairs(xs,ys)[i]==[xs[i],ys[i]]
 
 for every `0<=i<min({xs.size,ys.size})`."
shared Iterable<[First,Second],FirstAbsent|SecondAbsent> zipPairs<First,Second,FirstAbsent,SecondAbsent>
        (Iterable<First,FirstAbsent> firstElements, Iterable<Second,SecondAbsent> secondElements)
        given FirstAbsent satisfies Null
        given SecondAbsent satisfies Null
        => mapPairs((First first,Second second) => [first,second],
                firstElements, secondElements);

"Given a stream of values, and a stream of tuples, produce
 a new stream of tuples formed by prepending the values in
 the first stream to the tuples in the second stream. The 
 length of the resulting stream is the length of the shorter 
 of the two given streams.
 
 Thus:
 
     zip(heads, tails)[i]==[heads[i],*tails[i]]
 
 for every `0<=i<min({heads.size,tails.size})`."
shared Iterable<Tuple<Element|Head,Head,Tail>,HeadAbsent|TailAbsent> zip<Element,Head,Tail,HeadAbsent,TailAbsent>
        (Iterable<Head,HeadAbsent> heads, Iterable<Tail,TailAbsent> tails)
        given Tail satisfies Element[]
        given HeadAbsent satisfies Null
        given TailAbsent satisfies Null
        => mapPairs(Tuple<Element|Head,Head,Tail>, heads, tails);

"Given two streams, form a new stream by applying a 
 function to the arguments in the given streams. The 
 length of the resulting stream is the length of 
 the shorter of the two given streams.
 
 Thus:
 
     zip(xs,ys,fun)[i]==fun(xs[i],ys[i])
 
 for every `0<=i<min({xs.size,ys.size})`."
shared {Result*} zip<Result,FirstArgument,SecondArgument>(
    Result zipping(FirstArgument firstArg, SecondArgument secondArg),
    {FirstArgument*} firstArguments, 
    {SecondArgument*} secondArguments
) {
    object iterable satisfies {Result*} {
        shared actual Iterator<Result> iterator() {
            value first = firstArguments.iterator();
            value second = secondArguments.iterator();
            object iterator satisfies Iterator<Result> {
                shared actual Result|Finished next() {
                    if (!is Finished firstArg=first.next(),
                        !is Finished secondArg=second.next()) {
                        return zipping(firstArg,secondArg);
                    }
                    else {
                        return finished;
                    }
                }
            }
            return iterator;
        }
    }
    return iterable;
}

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
shared {<Key->Item>*} zipEntries<Key,Item>
        ({Key*} keys, {Item*} items)
        given Key satisfies Object
        given Item satisfies Object
        => zip(Entry<Key,Item>, keys, items);

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
shared {[First,Second]*} zipPairs<First,Second>
        ({First*} firstElements, {Second*} secondElements)
        => zip((First first,Second second)=>[first,second],
                firstElements, secondElements);

"Given a stream of values, and a stream of tuples, produce
 a new stream of tuples formed by prepending the values in
 the first stream to the tuples in the second stream. The 
 length of the resulting stream is the length of the shorter 
 of the two given streams.
 
 Thus:
 
     combine(heads, tails)[i]==[heads[i],*tails[i]]
 
 for every `0<=i<min({heads.size,tails.size})`."
shared {Tuple<Element|Head,Head,Tail>*} combine<Element,Head,Tail>
        ({Head*} heads, {Tail*} tails)
        given Tail satisfies Element[]
        => zip(Tuple<Element|Head,Head,Tail>, heads, tails);

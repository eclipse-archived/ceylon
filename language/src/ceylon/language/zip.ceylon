"Given two streams, form a new stream by applying a 
 function to the arguments in the given streams. The 
 length of the resulting stream is the length of 
 the shorter of the two given streams. 
 
 Thus:
 
     zip(xs,ys,fun)[i]==fun(xs[i],ys[i])
 
 for every `0<=i<min({xs.size,ys.size})`."
shared {Result*} zip<Result,FirstArgument,SecondArgument>(
    {FirstArgument*} firstArguments, 
    {SecondArgument*} secondArguments, 
    Result zipping(FirstArgument firstArg, SecondArgument secondArg)
)
        given FirstArgument satisfies Object
        given SecondArgument satisfies Object {
    value iter = secondArguments.iterator();
    return [ for (firstArg in firstArguments) 
                if (!is Finished secondArg=iter.next()) 
                    zipping(firstArg,secondArg) ];
}

"Given two streams, form a new stream consisting of
 all entries where, for any given index in the resulting
 stream, the key of the entry is the element occurring 
 at the same index in the first stream, and the item is 
 the element occurring at the same index in the second 
 stream. The length of the resulting stream is the 
 length of the shorter of the two given streams. 
 
 Thus:
 
     zipEntries(xs,ys)[i]==xs[i]->ys[i]
 
 for every `0<=i<min({xs.size,ys.size})`."
shared {<Key->Item>*} zipEntries<Key,Item>({Key*} keys, {Item*} items)
        given Key satisfies Object
        given Item satisfies Object
        => zip(keys, items, Entry<Key,Item>);

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
shared {[First,Second]*} zipPairs<First,Second>(
    {First*} firstElements, 
    {Second*} secondElements
)
        given First satisfies Object
        given Second satisfies Object
        => zip(firstElements, secondElements,
                (First first,Second second)=>[first,second]);

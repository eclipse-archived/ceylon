"Given two streams, form a new stream by applying a 
 function to the arguments in the given streams. The 
 length of the resulting stream is the length of 
 the shorter of the two given streams.
 
 Thus:
 
     mapPairs(xs,ys,fun)[i]==fun(xs[i],ys[i])
 
 for every `0<=i<min({xs.size,ys.size})`."
shared {Result*} mapPairs<Result,FirstArgument,SecondArgument>(
    Result collecting(FirstArgument firstArg, SecondArgument secondArg),
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
                        return collecting(firstArg,secondArg);
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

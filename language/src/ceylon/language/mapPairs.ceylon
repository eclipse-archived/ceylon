"Given two streams, form a new stream by applying a 
 function to the arguments in the given streams. The 
 length of the resulting stream is the length of 
 the shorter of the two given streams.
 
 Thus:
 
     mapPairs(fun,xs,ys)[i]==fun(xs[i],ys[i])
 
 for every `0<=i<min({xs.size,ys.size})`."
shared Iterable<Result,FirstAbsent|SecondAbsent> mapPairs<Result,FirstArgument,SecondArgument,FirstAbsent,SecondAbsent>(
    Result collecting(FirstArgument firstArg, SecondArgument secondArg),
    Iterable<FirstArgument,FirstAbsent> firstArguments,
    Iterable<SecondArgument,SecondAbsent> secondArguments
)
        given FirstAbsent satisfies Null
        given SecondAbsent satisfies Null {
    object iterable satisfies Iterable<Result,FirstAbsent|SecondAbsent> {
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
                string => "``outer.string``.iterator()";
            }
            return iterator;
        }
        shared actual String string => "mapPairs(..., ``firstArguments.string``, ``secondArguments.string``)";
    }
    return iterable;
}

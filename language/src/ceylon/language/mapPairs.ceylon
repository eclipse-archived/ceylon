"Given two streams, form a new stream by applying a binary
 [[mapping function|collecting]] to the arguments in the 
 given streams. The length of the resulting stream is the 
 length of the shorter of the two given streams.
 
 Thus:
 
     mapPairs(fun, xs, ys)[i] == fun(xs[i], ys[i])
 
 for every `0<=i<smallest(xs.size,ys.size)`."
shared Iterable<Result,FirstAbsent|SecondAbsent> 
mapPairs<Result,First,Second,FirstAbsent,SecondAbsent>(
    "The mapping function to apply to the pair of elements"
    Result collecting(First firstArg, Second secondArg),
    Iterable<First,FirstAbsent> firstArguments,
    Iterable<Second,SecondAbsent> secondArguments)
        given FirstAbsent satisfies Null
        given SecondAbsent satisfies Null {
    object iterable 
            satisfies Iterable<Result,FirstAbsent|SecondAbsent> {
        shared actual Iterator<Result> iterator() {
            object iterator 
                    satisfies Iterator<Result> {
                value first = firstArguments.iterator();
                value second = secondArguments.iterator();
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
    }
    return iterable;
}

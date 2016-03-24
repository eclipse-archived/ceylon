"Given two streams, form a new stream by applying a binary
 [[mapping function|collecting]] to pairs of elements in the 
 given streams.  If one of the streams is longer than the 
 other, simply ignore additional elements of the longer 
 stream with no pair in the other stream. The length of the 
 resulting stream is the length of the shorter of the two 
 given streams.
 
 For any given streams `xs` and `ys`, and mapping function 
 `f`, `mapPairs()` may be defined in terms of 
 [[Iterable.map]], [[zipPairs]], and [[unflatten]]:
 
     mapPairs(f, xs, ys) == zipPairs(xs, ys).map(unflatten(f))
 
 For example the expression
 
     mapPairs((Float x, Float y) => (x^2+y^2)^0.5, 
             {1.0, 2.0, 1.0}, {1.0, 1.0, 2.0})
     
 evaluates to the stream `{ 1.0, 3.0, 3.0 }`."
tagged("Streams")
shared Iterable<Result,FirstAbsent|SecondAbsent> 
mapPairs<Result,First,Second,FirstAbsent,SecondAbsent>(
    "The mapping function to apply to the pair of elements."
    Result collecting(First first, Second second),
    Iterable<First,FirstAbsent> firstIterable,
    Iterable<Second,SecondAbsent> secondIterable)
        given FirstAbsent satisfies Null
        given SecondAbsent satisfies Null {
    object iterable 
            satisfies Iterable<Result,FirstAbsent|SecondAbsent> {
        shared actual Iterator<Result> iterator() {
            object iterator 
                    satisfies Iterator<Result> {
                value firstIter = firstIterable.iterator();
                value secondIter = secondIterable.iterator();
                shared actual Result|Finished next() {
                    if (!is Finished first=firstIter.next(),
                        !is Finished second=secondIter.next()) {
                        return collecting(first,second);
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

"Given two streams, return the first pair of elements in the 
 given streams that satisfies the given binary [[predicate
 function|selecting]], or null if no pair of elements 
 satisfies the predicate. If one of the streams is longer 
 than the other, simply ignore additional elements of the 
 longer stream with no pair in the other stream.
 
 For any given streams `xs` and `ys`, and predicate function 
 `p`, `findPair()` may be defined in terms of 
 [[Iterable.find]], [[zipPairs]], and [[unflatten]]:
 
     findPair(p, xs, ys) == zipPairs(xs, ys).find(unflatten(p))"
tagged("Streams")
shared [First,Second]? findPair<First,Second>(
    "The binary predicate function to apply to each pair of 
     elements."
    Boolean selecting(First first, Second second),
    {First*} firstIterable, {Second*} secondIterable) {
    value firstIter = firstIterable.iterator();
    value secondIter = secondIterable.iterator();
    while (!is Finished first=firstIter.next(),
           !is Finished second=secondIter.next()) {
        if (selecting(first,second)) {
            return [first,second];
        }
    }
    return null;
}

"Given two streams, return `true` if every pair of elements 
 in the given streams satisfies the given binary [[predicate
 function|selecting]], or `false` otherwise. If one of the
 streams is longer than the other, simply ignore additional 
 elements of the longer stream with no pair in the other 
 stream. If either stream is empty, return `true`.
 
 For any given streams `xs` and `ys`, and predicate function 
 `p`, `everyPair()` may be defined in terms of 
 [[Iterable.every]], [[zipPairs]], and [[unflatten]]:
 
     everyPair(p, xs, ys) == zipPairs(xs, ys).every(unflatten(p))"
see (`function corresponding`,
     `function anyPair`)
tagged("Streams")
shared Boolean everyPair<First,Second>(
    "The binary predicate function to apply to each pair of 
     elements."
    Boolean selecting(First first, Second second),
    {First*} firstIterable, {Second*} secondIterable) {
    value firstIter = firstIterable.iterator();
    value secondIter = secondIterable.iterator();
    while (!is Finished first=firstIter.next(),
        !is Finished second=secondIter.next()) {
        if (!selecting(first,second)) {
            return false;
        }
    }
    return true;
}

"Given two streams, return `true` if some pair of elements 
 in the given streams satisfies the given binary [[predicate
 function|selecting]], or `false` otherwise. If one of the
 streams is longer than the other, simply ignore additional 
 elements of the longer stream with no pair in the other 
 stream. If either stream is empty, return `false`.
 
 For any given streams `xs` and `ys`, and predicate function 
 `p`, `anyPair()` may be defined in terms of 
 [[Iterable.any]], [[zipPairs]], and [[unflatten]]:
 
     anyPair(p, xs, ys) == zipPairs(xs, ys).any(unflatten(p))"
see (`function everyPair`)
tagged("Streams")
shared Boolean anyPair<First,Second>(
    "The binary predicate function to apply to each pair of 
     elements."
    Boolean selecting(First first, Second second),
    {First*} firstIterable, {Second*} secondIterable) {
    value firstIter = firstIterable.iterator();
    value secondIter = secondIterable.iterator();
    while (!is Finished first=firstIter.next(),
           !is Finished second=secondIter.next()) {
        if (selecting(first,second)) {
            return true;
        }
    }
    return false;
}

"Given two streams, return the result of applying the given 
 [[accumulating function|accumulating]] to each pair of 
 elements of the given streams in turn. If one of the 
 streams is longer than the other, simply ignore additional 
 elements of the longer stream with no pair in the other 
 stream.
 
 For any given streams `xs` and `ys`, initial value `z`, and
 combining function `f`, `foldPairs()` may be defined in 
 terms of [[Iterable.fold]], [[zipPairs]], and [[unflatten]]:
 
     foldPairs(z, f, xs, ys) == zipPairs(xs, ys).fold(z)(unflatten(f))"
tagged("Streams")
shared Result foldPairs<Result,First,Second>(
    Result initial,
    "The accumulating function to apply to each pair of 
     elements."
    Result accumulating(Result partial, 
                        First first, Second second),
    {First*} firstIterable, {Second*} secondIterable) {
    value firstIter = firstIterable.iterator();
    value secondIter = secondIterable.iterator();
    variable value partial = initial;
    while (!is Finished first=firstIter.next(),
           !is Finished second=secondIter.next()) {
        partial = accumulating(partial, first, second);
    }
    return partial;
}

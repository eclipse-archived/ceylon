/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
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
 
     mapPairs(xs, ys, f) == zipPairs(xs, ys).map(unflatten(f))
 
 For example the expression
 
     mapPairs({3.0, 5.0, 6.0, 9.0}, {4.0, 12.0, 8.0, 12.0},
            (Float x, Float y) => (x^2+y^2)^0.5)
     
 evaluates to the stream `{ 5.0, 13.0, 10.0, 15.0 }`."
tagged("Streams")
since("1.4.0")
shared Iterable<Result,FirstAbsent|SecondAbsent> 
mapPairs<Result,First,Second,FirstAbsent,SecondAbsent>
    (Iterable<First,FirstAbsent> firstIterable,
     Iterable<Second,SecondAbsent> secondIterable,
     "The mapping function to apply to the pair of elements."
     Result collecting(First first, Second second))
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
                    if (!is Finished first = firstIter.next(),
                        !is Finished second = secondIter.next()) {
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
 
     findPair(xs, ys, p) == zipPairs(xs, ys).find(unflatten(p))"
tagged("Streams")
since("1.1.0")
shared [First,Second]? findPair<First,Second>
    ({First*} firstIterable, {Second*} secondIterable,
     "The binary predicate function to apply to each pair of 
      elements."
     Boolean selecting(First first, Second second)) {
    value firstIter = firstIterable.iterator();
    value secondIter = secondIterable.iterator();
    while (!is Finished first = firstIter.next(),
           !is Finished second = secondIter.next()) {
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
 
     everyPair(xs, ys, p) == zipPairs(xs, ys).every(unflatten(p))"
see (function corresponding,
     function anyPair)
tagged("Streams")
since("1.1.0")
shared Boolean everyPair<First,Second>
    ({First*} firstIterable, {Second*} secondIterable,
     "The binary predicate function to apply to each pair of 
      elements."
     Boolean selecting(First first, Second second)) {
    value firstIter = firstIterable.iterator();
    value secondIter = secondIterable.iterator();
    while (!is Finished first = firstIter.next(),
           !is Finished second = secondIter.next()) {
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
 
     anyPair(xs, ys, p) == zipPairs(xs, ys).any(unflatten(p))"
see (function everyPair)
tagged("Streams")
since("1.1.0")
shared Boolean anyPair<First,Second>
    ({First*} firstIterable, {Second*} secondIterable,
     "The binary predicate function to apply to each pair of 
      elements."
     Boolean selecting(First first, Second second)) {
    value firstIter = firstIterable.iterator();
    value secondIter = secondIterable.iterator();
    while (!is Finished first = firstIter.next(),
           !is Finished second = secondIter.next()) {
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
 
     foldPairs(xs, ys, z, f) == zipPairs(xs, ys).fold(z)(unflatten(f))"
tagged("Streams")
since("1.1.0")
shared Result foldPairs<Result,First,Second>
    ({First*} firstIterable, {Second*} secondIterable,
     "The initial value of the accumulator."
     Result initial,
     "The accumulating function to apply to each pair of 
      elements."
     Result accumulating(Result partial, 
                         First first, Second second)) {
    value firstIter = firstIterable.iterator();
    value secondIter = secondIterable.iterator();
    variable value partial = initial;
    while (!is Finished first = firstIter.next(),
           !is Finished second = secondIter.next()) {
        partial = accumulating(partial, first, second);
    }
    return partial;
}

"Given two streams, call the given function for each
 corresponding pair of elements. If one of the streams 
 is longer than the other, simply ignore additional 
 elements of the longer stream with no pair in the other 
 stream."
tagged("Streams")
since("1.4.0")
shared void eachPair<First,Second>
        ({First*} firstIterable, {Second*} secondIterable,
         "Function called for each pair of elements."
         void step(First first, Second second)) {
    value firstIter = firstIterable.iterator();
    value secondIter = secondIterable.iterator();
    while (!is Finished first = firstIter.next(),
           !is Finished second = secondIter.next()) {
        step(first,second);
    }
}

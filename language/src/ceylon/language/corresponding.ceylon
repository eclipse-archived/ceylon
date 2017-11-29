/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Compares corresponding elements of the given streams using 
 the given [[binary predicate function|comparing]]. Two 
 elements are considered _corresponding_ if they occupy the
 same position in their respective streams. Returns `true` 
 if and only if:
 
 - the two streams have the same number of elements, and 
 - if the predicate is satisfied for every pair of 
   corresponding elements.
 
 Returns `false` otherwise. If both streams are empty, 
 return `true`.
 
 For example:
 
     corresponding({ 1, 2, 3, 4 }, 1:4)
 
 and:
 
     corresponding({ 1, 2, 3, 4 }, \"1234\",
            (i, c) => i.string==c.string)
 
 both evaluate to `true`."
see (function everyPair, 
     function compareCorresponding)
tagged("Comparisons", "Streams")
since("1.1.0")
shared Boolean corresponding<First,Second>
    ({First*} firstIterable, {Second*} secondIterable,
     "The predicate function that compares an element of the
      [[first stream|firstIterable]] with the corresponding 
      element of the [[second stream|secondIterable]].
      
      By default, the elements are compared by a predicate
      function that returns `true` if and only if the 
      elements are [[equal|Object.equals]] or both `null`."
     Boolean comparing(First first, Second second)
            => if (exists first, exists second) 
                    then first==second
                    else !first exists 
                      && !second exists) {
    value firstIter = firstIterable.iterator();
    value secondIter = secondIterable.iterator();
    while (true) {
        value first = firstIter.next();
        value second = secondIter.next();
        if (!is Finished first, !is Finished second) {
            if (!comparing(first, second)) {
                return false;
            }
        }
        else {
            return first is Finished 
                && second is Finished;
        }
    }
}

"Compares corresponding elements of the given streams using 
 the given [[comparison function|comparing]]. Two elements 
 are considered _corresponding_ if they occupy the same 
 position in their respective streams. Returns:
 
 - the result of the given comparison for the earliest pair
   of corresponding elements whose comparison does not 
   evaluate to [[equal]], or, otherwise, if every comparison 
   of corresponding pairs of elements produces `equal`,
 - [[smaller]], if the first stream produces fewer elements 
   than the second stream, 
 - [[larger]], if the first stream produces more elements 
   than the second stream, or
 - [[equal]] if the two streams produce the same number of 
   elements.
 
 If both streams are empty, return `equal`.
 
 For example:
 
     compareCorresponding({ 1, 2, 2, 5 }, 1:4)
            ((i, j) => i<=>j)
 
 and:
 
     compareCorresponding({ 1, 2, 3 }, 1:4)
            ((i, j) => i<=>j)
 
 both evaluate to `smaller`."
see (function corresponding)
tagged("Comparisons", "Streams")
since("1.3.0")
shared Comparison compareCorresponding<First,Second>
    ({First*} firstIterable, {Second*} secondIterable,
     "The comparison function that compares an element of the
      [[first stream|firstIterable]] with the corresponding 
      element of the [[second stream|secondIterable]]."
     Comparison comparing(First first, Second second)) {
    value firstIter = firstIterable.iterator();
    value secondIter = secondIterable.iterator();
    while (true) {
        value first = firstIter.next();
        value second = secondIter.next();
        if (!is Finished first, !is Finished second) {
            value comp = comparing(first, second);
            if (comp!=equal) {
                return comp;
            }
        }
        else {
            value firstFinished = first is Finished;
            value secondFinished = second is Finished;
            return if (firstFinished && !secondFinished) 
                    then smaller
              else if (!firstFinished && secondFinished) 
                    then larger
              else equal;
        }
    }
}


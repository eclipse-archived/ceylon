"Compares corresponding elements of the given streams using 
 the given [[binary predicate function|comparing]]. Return 
 `true` if the two streams have the same number of elements 
 and if the predicate is satisfied for every pair of 
 corresponding elements. Return `false` otherwise."
shared Boolean corresponding<First,Second>(
    {First*} firstIterable, {Second*} secondIterable,
    "The predicate function that compares an element of the
     [[first stream|firstIterable]] with the corresponding 
     element of the [[second stream|secondIterable]]."
    Boolean comparing(First first, Second second)) {
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
            return first is Finished && second is Finished;
        }
    }
}


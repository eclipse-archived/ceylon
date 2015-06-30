"Compares corresponding elements of the given streams using 
 the given [[binary predicate function|comparing]]. Two 
 elements are considered _corresponding_ if they occupy the
 same position in their respective streams. Returns `true` 
 if and only if:
 
 - the two streams have the same number of elements, and 
 - if the predicate is satisfied for every pair of 
   corresponding elements.
 
 Returns `false` otherwise. If both streams are empty, 
 return `true`."
see (`function everyPair`)
shared Boolean corresponding<First,Second>(
    {First*} firstIterable, {Second*} secondIterable,
    "The predicate function that compares an element of the
     [[first stream|firstIterable]] with the corresponding 
     element of the [[second stream|secondIterable]]."
    Boolean comparing(First first, Second second)
            => if (exists first, exists second) 
                    then first==second
                    else !first exists && 
                         !second exists) {
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


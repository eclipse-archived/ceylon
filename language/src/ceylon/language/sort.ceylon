"Sort the given elements according to their 
 [[natural order|Comparable]], returning a new 
 [[sequence|Sequential]].
 
 Note that [[Iterable.sort]] may be used to sort any stream
 according to a given comparator function."
see (`interface Comparable`,
     `function Iterable.sort`)
tagged("Streams", "Comparisons")
shared Element[] sort<Element>({Element*} elements) 
        given Element satisfies Comparable<Element> {
    value array = Array(elements);
    if (array.empty) {
        return [];
    }
    else {
        array.sortInPlace(byIncreasing(identity<Element>));
        return ArraySequence(array);
    }
}
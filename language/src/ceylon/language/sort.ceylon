"Sort the given elements according to their 
 [[natural order|Comparable]], returning a new 
 [[sequence|Sequential]]."
see (`interface Comparable`)
shared Element[] sort<Element>({Element*} elements) 
        given Element satisfies Comparable<Element> {
    value array = Array(elements);
    if (array.empty) {
        return [];
    }
    else {
        array.sortInPlace(byIncreasing((Element e) => e));
        return ArraySequence(array);
    }
}
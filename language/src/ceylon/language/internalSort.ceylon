Element[] internalSort<Element>(
        Comparison comparing(Element x, Element y), 
        {Element*} elements) {
    value array = Array(elements);
    if (array.empty) {
        return [];
    }
    array.sortInPlace(comparing);
    return ASequence(array);
}

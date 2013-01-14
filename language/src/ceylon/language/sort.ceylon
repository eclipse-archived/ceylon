doc "Sort a given elements, returning a new sequence."
see (Comparable)
shared Element[] sort<Element>(Element* elements) 
        given Element satisfies Comparable<Element> {
    return internalSort(elements, byIncreasing(x, y));
}

native Element[] internalSort<Element>(Element* elements, Comparison? comparing(Element x, Element y)) {
    
}

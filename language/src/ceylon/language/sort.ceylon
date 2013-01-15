doc "Sort a given elements, returning a new sequence."
see (Comparable)
shared Element[] sort<Element>(Element* elements) 
        given Element satisfies Comparable<Element> {
    return internalSort(byIncreasing((Element e) => e), elements);
}

native Element[] internalSort<Element>(
        Comparison? comparing(Element x, Element y), 
        {Element*} elements);

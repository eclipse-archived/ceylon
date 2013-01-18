doc "Sort a given elements, returning a new sequence."
see (Comparable)
shared Element[] sort<Element>({Element*} elements) 
        given Element satisfies Comparable<Element>
        => internalSort(byIncreasing((Element e) => e), elements);

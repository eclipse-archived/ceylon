doc "Sort a given elements, returning a new sequence."
see (Comparable)
shared native Element[] sort<Element>(Element* elements) 
        given Element satisfies Comparable<Element>;

doc "Appends an element to a given sequence, returning a
     new, nonempty sequence."
see (Sequence)
shared Sequence<Element> append<Element>(Element[] sequence, 
                                         Element element) {
    if (nonempty sequence) {
        return sequence.withTrailing(element);
    }
    else {
        return { element };
    }
}
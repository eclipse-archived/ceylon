doc "Prepend an element to a given sequence, returning a
     new, nonempty sequence."
see (Sequence)
shared Sequence<Element> prepend<Element>(Element[] sequence, 
                                         Element element) {
    if (nonempty sequence) {
        return sequence.withLeading(element);
    }
    else {
        return { element };
    }
}
doc "Count the elements for which the block
     evaluates to true."
shared Natural count<X>(iterated Iterable<X> elements,
                        Boolean where(coordinated X x)) {
    variable Natural count := 0;
    for (X x in elements) {
        if ( where(x) ) {
            ++count;
        }
    }
    return count;
}
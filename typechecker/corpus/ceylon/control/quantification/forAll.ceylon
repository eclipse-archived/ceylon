doc "Return true iff for every element, the block
     evaluates to true."
shared Boolean forAll<X>(iterated Iterable<X> elements,
                         Boolean every(coordinated X x)) {
    for (X x in elements) {
        if ( !every(x) ) {
            return false;
        }
    }
    return true;
}
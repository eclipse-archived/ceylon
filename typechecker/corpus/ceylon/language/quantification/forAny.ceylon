doc "Return true iff for some element, the block
     evaluates to true."
shared Boolean forAny<X>(iterated Iterable<X> elements,
                         Boolean some(coordinated X x)) {
    Boolean where(X x) { return !some(x); }
    return !forAll(elements, where);
}
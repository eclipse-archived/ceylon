doc "Iterate elements and select those for which
     the first block evaluates to true. For each of
     these, evaluate the second block. Build a list
     of the resulting values, ordered using the
     third block, if specified."
shared List<Y> from<X,Y,C>(iterated Iterable<X> elements,
                           Boolean where(coordinated X x) = always,
                           Y select(coordinated X x) = id,
                           C by(coordinated X x) = id)
        given C satisfies Comparable<C> {
    OpenList<Y> list = ArrayList<Y>();
    for (X x in elements) {
        if ( where(x) ) {
            list.append( select(x) );
        }
    }
    list.sort(by);
    return list;
}
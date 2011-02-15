doc "Return the first element for which the first
     block evaluates to true, or the result of
     evaluating the second block, if no such
     element is found."
shared X first<X>(iterated Iterable<X> elements,
                  Boolean where(coordinated X x),
                  X otherwise()=get(null)) {
    if (exists X first = first(elements, where)) {
        return first;
    }
    else {
        return otherwise();
    }
}
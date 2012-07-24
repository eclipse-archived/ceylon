doc "Produces a sequence of each index to element `Entry` 
     for the given sequence of values."
shared Iterable<Entry<Integer,Element&Object>> entries<Element>(Element... elements) {
    return elements.indexed;
}

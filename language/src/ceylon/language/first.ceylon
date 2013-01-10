doc "The first of the given elements (usually a comprehension),
     if any."
shared Element? first<Element>(Element* elements) {
    if (!is Finished first = elements.iterator.next()) {
        return first;
    }
    else {
       return null;
    }
}

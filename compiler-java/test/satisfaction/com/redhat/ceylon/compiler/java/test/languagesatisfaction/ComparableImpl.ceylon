class ComparableImpl<Element>() satisfies Comparable<Element> 
    given Element satisfies Comparable<Element> {
    shared actual Comparison compare(Element element) {
        return equal;
    }
}
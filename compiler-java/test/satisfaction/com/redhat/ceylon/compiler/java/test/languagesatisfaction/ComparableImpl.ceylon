class ComparableImpl<Element>()
    satisfies Comparable<ComparableImpl<Element>> {
    shared actual Comparison compare(ComparableImpl<Element> element) {
        return equal;
    }
}
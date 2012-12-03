class ListImpl<out Element>() extends Object() satisfies List<Element> {
    shared actual ListImpl<Element> clone = bottom;
    shared actual ListImpl<Element> segment(Integer element, Integer length) {
        return bottom;
    }
    shared actual ListImpl<Element> span(Integer from, Integer to) {
        return bottom;
    }
    shared actual ListImpl<Element> spanFrom(Integer from) {
        return bottom;
    }
    shared actual ListImpl<Element> spanTo(Integer to) {
        return bottom;
    }
    shared actual Integer? lastIndex {
        return bottom;
    }
    shared actual Element? item(Integer index) {
        return bottom;
    }
    shared actual ListImpl<Element> reversed {
        throw;
    }
}
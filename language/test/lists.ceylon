class TestList<Element>(Element... items) satisfies List<Element> {
    value elems = items.sequence;
    shared actual Boolean equals(Object other) { return false; }
    shared actual Element? item(Integer x) { return elems[x]; }
    shared actual TestList<Element> reversed { return TestList(elems.reversed...); }
    shared actual Integer hash { return elems.hash; }
    shared actual Integer? lastIndex { return elems.lastIndex; }
    shared actual TestList<Element> span(Integer a, Integer? b) { return TestList(elems.span(a, b)...); }
    shared actual TestList<Element> segment(Integer a, Integer b) { return TestList(elems.segment(a, b)...); }
    shared actual TestList<Element> clone { return TestList(items...); }
}

void lists() {
    value a = TestList(1,2,3,4);
    value b = TestList(1,2,3,4,5,6,7,8);
}

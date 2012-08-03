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
    value b = LazyList(1,2,3,4,5,6,7,8);
    //withLeading
    assert({}.withLeading("A")=={"A"}, "Empty.withLeading(A)");
    assert({}.withLeading("foo").size==1, "{}.withLeading.size");
    assert(array().withLeading(1)=={1}, "empty array.withLeading(1)");
    assert(array(1,2).withLeading("A")=={"A",1,2}, "Array.withLeading(a)");
    assert({1,2}.withLeading("a")=={"a",1,2}, "Sequence.withLeading(a)");
    assert({1,2}.withLeading("foo").size==3, "Sequence.withLeading.size");
    assert(Singleton(1).withLeading("a")=={"a",1}, "Singleton.withLeading(a)");
    assert((1..3).withLeading("a")=={"a",1,2,3}, "Range.withLeading(a)");
    assert((1..3).withLeading(0).first==0, "Range.withLeading(a).first");
    assert((1..3).withLeading(0).last==3, "Range.withLeading(a).last");
    assert("abc".withLeading(1)=={1,`a`,`b`,`c`}, "String.withLeading(1)");
    assert("".withLeading(1)=={1}, "\"\".withLeading(1)");

    //withTrailing
    assert({}.withTrailing("A")=={"A"}, "Empty.withTrailing(A)");
    assert({}.withTrailing("foo").size==1, "{}.withTrailing.size");
    assert(array().withTrailing(1)=={1}, "empty array.withTrailing(1)");
    assert(array(1,2).withTrailing("A")=={1,2,"A"}, "Array.withTrailing(a)");
    assert({1,2}.withTrailing("a")=={1,2,"a"}, "Sequence.withTrailing(a)");
    assert({1,2}.withTrailing("foo").size==3, "Sequence.withTrailing.size");
    assert(Singleton(1).withTrailing("a")=={1,"a"}, "Singleton.withTrailing(a)");
    assert((1..3).withTrailing(4)=={1,2,3,4}, "Range.withTrailing(a)");
    assert((1..3).withTrailing(4).first==1, "Range.withTrailing(a).first");
    assert((1..3).withTrailing(4).last==4, "Range.withTrailing(a).last");
    assert("abc".withTrailing(1)=={`a`,`b`,`c`,1}, "String.withTrailing(1)");
    assert("".withTrailing(1)=={1}, "\"\".withTrailing(1)");

    //LazyList
    assert(b.size == 8, "LazyList.size");
    if (exists i=b.lastIndex) {
        assert(i==7, "LazyList.lastIndex");
    } else { fail("LazyList.lastIndex"); }
    if (exists e=b[4]) {
        assert(e==5, "LazyList.item");
    } else { fail("LazyList.item"); }
    assert(b.span(2,4)=={3,4,5}, "LazyList.span & equals");
    assert(b.segment(2,3)=={3,4,5}, "LazyList.segment");
    if (exists e=b.findLast((Integer x) true)) {
        assert(e==8, "LazyList.findLast");
    } else { fail("LazyList.findLast"); }
    if (exists e=b.first) {
        assert(e==1, "LazyList.first");
    } else { fail("LazyList.last"); }
    if (exists e=b.last) {
        assert(e==8, "LazyList.last");
    } else { fail("LazyList.last"); }
    assert(b.reversed==8..1, "LazyList.reversed");
}

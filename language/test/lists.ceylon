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
    //withLeading
    assert({}.withLeading()=={}, "Empty.withLeading()");
    assert({}.withLeading("A")=={"A"}, "Empty.withLeading(A)");
    assert({}.withLeading("A","B")=={"A","B"}, "Empty.withLeading(A,B)");
    assert(array().withLeading()=={}, "empty array.withLeading()");
    assert(array().withLeading(1,2)=={1,2}, "empty array.withLeading(1,2)");
    assert(array(1,2).withLeading()=={1,2}, "Array.withLeading()");
    assert(array(1,2).withLeading("A","B")=={"A","B",1,2}, "Array.withLeading(a,b)");
    assert({1,2}.withLeading()=={1,2}, "Sequence.withLeading()");
    assert({1,2}.withLeading("a","b")=={"a","b",1,2}, "Sequence.withLeading()");
    assert(Singleton(1).withLeading()=={1}, "Singleton.withLeading()");
    assert(Singleton(1).withLeading("a","b")=={"a","b",1}, "Singleton.withLeading(a,b)");
    assert((1..3).withLeading()==1..3, "Range.withLeading()");
    assert((1..3).withLeading("a","b")=={"a","b",1,2,3}, "Range.withLeading(a,b)");
    assert((1..3).withLeading(0).first==0, "Range.withLeading(a).first");
    assert("abc".withLeading()=="abc", "String.withLeading()");
    assert("abc".withLeading(1,2)=={1,2,`a`,`b`,`c`}, "String.withLeading(1,2)");
    assert("".withLeading()=="", "\"\".withLeading()");
    assert("".withLeading(1)=={1}, "\"\".withLeading(1)");

    //withTrailing
    assert({}.withTrailing()=={}, "Empty.withTrailing()");
    assert({}.withTrailing("A")=={"A"}, "Empty.withTrailing(A)");
    assert({}.withTrailing("A","B")=={"A","B"}, "Empty.withTrailing(A,B)");
    assert(array().withTrailing()=={}, "empty array.withTrailing()");
    assert(array().withTrailing(1,2)=={1,2}, "empty array.withTrailing(1,2)");
    assert(array(1,2).withTrailing()=={1,2}, "Array.withTrailing()");
    assert(array(1,2).withTrailing("A","B")=={1,2,"A","B"}, "Array.withTrailing(a,b)");
    assert({1,2}.withTrailing()=={1,2}, "Sequence.withTrailing()");
    assert({1,2}.withTrailing("a","b")=={1,2,"a","b"}, "Sequence.withTrailing()");
    assert(Singleton(1).withTrailing()=={1}, "Singleton.withTrailing()");
    assert(Singleton(1).withTrailing("a","b")=={1,"a","b"}, "Singleton.withTrailing(a,b)");
    assert((1..3).withTrailing()==1..3, "Range.withTrailing()");
    assert((1..3).withTrailing()==1..3, "Range.withTrailing()");
    assert((1..3).withTrailing(4).first==1, "Range.withTrailing(a).first");
    assert("abc".withTrailing()=="abc", "String.withTrailing()");
    assert("abc".withTrailing(1,2)=={`a`,`b`,`c`,1,2}, "String.withTrailing(1,2)");
    assert("".withTrailing()=="", "\"\".withTrailing()");
    assert("".withTrailing(1)=={1}, "\"\".withTrailing(1)");
}

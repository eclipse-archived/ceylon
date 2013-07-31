class TestList<Element>(Element* elems) satisfies List<Element> {
    shared actual Boolean equals(Object other) => (super of List<Element>).equals(other);
    shared actual Element? get(Integer x) => elems[x];
    shared actual TestList<Element> reversed => TestList(*elems.reversed);
    shared actual TestList<Element> rest => TestList(*elems.rest);
    shared actual Integer hash => (super of List<Element>).hash;
    shared actual Integer? lastIndex => elems.lastIndex;
    shared actual TestList<Element> span(Integer a, Integer b) => TestList(*elems.span(a, b));
    shared actual TestList<Element> spanFrom(Integer a) => TestList(*elems.spanFrom(a));
    shared actual TestList<Element> spanTo(Integer b) => TestList(*elems.spanTo(b));
    shared actual TestList<Element> segment(Integer a, Integer b) => TestList(*elems.segment(a, b));
    shared actual TestList<Element> clone => TestList(*elems);
}

void lists() {
    value a = TestList(1,2,3,4);
    value b = LazyList({1,2,3,4,5,6,7,8});
    check(LazyList({}).size==0, "empty LazyList()");
    //withLeading
    check({}.withLeading("A")=={"A"}, "Empty.withLeading(A)");
    check({}.withLeading("foo").size==1, "{}.withLeading.size");
    check(array{}.withLeading(1)=={1}, "empty array.withLeading(1)");
    check(array{1,2}.withLeading("A")=={"A",1,2}, "Array.withLeading(a)`` array{1,2}.withLeading("A") ``");
    check([1,2].withLeading("a")=={"a",1,2}, "Sequence.withLeading(a)`` [1,2].withLeading("a") ``");
    check([1,2].withLeading("foo").size==3, "Sequence.withLeading.size`` [1,2].withLeading("foo").size ``");
    check(Singleton(1).withLeading("a")=={"a",1}, "Singleton.withLeading(a)`` Singleton(1).withLeading("a") ``");
    check((1..3).withLeading("a")=={"a",1,2,3}, "Range.withLeading(a)");
    check((1..3).withLeading(0).first==0, "Range.withLeading(a).first");
    check((1..3).withLeading(0).last==3, "Range.withLeading(a).last");
    check("abc".withLeading(1)=={1,'a','b','c'}, "String.withLeading(1)" +"abc".withLeading(1).string);
    check("".withLeading(1)=={1}, "\"\".withLeading(1)");
    check(b[100...]=={}, "LazyList[100...]");
    check(b[1...]==b.rest, "LazyList[1...]");

    //withTrailing
    check({}.withTrailing("A")=={"A"}, "Empty.withTrailing(A)");
    check({}.withTrailing("foo").size==1, "{}.withTrailing.size");
    check(array{}.withTrailing(1)=={1}, "empty array.withTrailing(1)");
    check(array{1,2}.withTrailing("A")=={1,2,"A"}, "Array.withTrailing(a)");
    check([1,2].withTrailing("a")=={1,2,"a"}, "Sequence.withTrailing(a)");
    check([1,2].withTrailing("foo").size==3, "Sequence.withTrailing.size");
    check(Singleton(1).withTrailing("a")=={1,"a"}, "Singleton.withTrailing(a)");
    check((1..3).withTrailing(4)=={1,2,3,4}, "Range.withTrailing(a)");
    check((1..3).withTrailing(4).first==1, "Range.withTrailing(a).first");
    check((1..3).withTrailing(4).last==4, "Range.withTrailing(a).last");
    check("abc".withTrailing(1)=={'a','b','c',1}, "String.withTrailing(1)");
    check("".withTrailing(1)=={1}, "\"\".withTrailing(1)");

    //LazyList
    check(b.size == 8, "LazyList.size");
    if (exists i=b.lastIndex) {
        check(i==7, "LazyList.lastIndex");
    } else { fail("LazyList.lastIndex"); }
    if (exists e=b[4]) {
        check(e==5, "LazyList.item");
    } else { fail("LazyList.item"); }
    check(b.span(-2,-1)=={}, "LazyList.span(-2,-1) & equals");
    check(b.span(-1,-2)=={}, "LazyList.span(-1,-2) & equals");
    check(b.span(-2,2)=={1,2,3}, "LazyList.span(-2,2) & equals");
    check(b.span(2,-2)=={3,2,1}, "LazyList.span(2,-2) & equals");
    check(b.span(2,4)=={3,4,5}, "LazyList.span(2,4) & equals: `` b.span(2,4) `` instead of {3,4,5}");
    check(b.span(6,10)=={7,8}, "LazyList.span(6,10) & equals: `` b.span(6,10) `` instead of {7,8}");
    check(b.spanFrom(4)=={5,6,7,8}, "LazyList.spanFrom(4) & equals: `` b.spanFrom(4) `` instead of {5,6,7,8}");
    check(b.spanFrom(10)=={}, "LazyList.spanFrom(10) & equals");
    check(b.spanTo(4)=={1,2,3,4,5}, "LazyList.spanTo(4) & equals");
    check(b.spanTo(-1)=={}, "LazyList.spanTo(-1) & equals");
    check(b.segment(2,3)=={3,4,5}, "LazyList.segment: `` b.segment(2,3) `` instead of {3,4,5}");
    if (exists e=b.findLast((Integer x) => true)) {
        check(e==8, "LazyList.findLast");
    } else { fail("LazyList.findLast"); }
    if (exists e=b.first) {
        check(e==1, "LazyList.first");
    } else { fail("LazyList.last"); }
    if (exists e=b.last) {
        check(e==8, "LazyList.last");
    } else { fail("LazyList.last"); }
    check(b.reversed==8..1, "LazyList.reversed");
    
    //#167
    value empty167 = TestList<Nothing>();
    check(empty167=={}, "empty167=={}");
    check({}==empty167, "{}==empty167");
    check(empty167.hash=={}.hash, "empty167.hash=={}.hash");
    //#197
    value nulled197 = TestList(1,2,3,null,5,6,7);
    check(nulled197.count((Integer? i) => i exists) == 6, "list with nulls (see #197)");

    check(b.longerThan(7), "List.longerThan");
    check(!b.longerThan(8), "List.longerThan");
    check(b.shorterThan(9), "List.shorterThan");
    check(!b.shorterThan(8), "List.shorterThan");
    check(b.initial(3) == [1,2,3], "List.initial");
    check(b.terminal(3) == [6,7,8], "List.terminal");
}

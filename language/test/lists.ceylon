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
    shared actual TestList<Element> clone() => TestList(*elems);
}

@test
shared void lists() {
    value a = TestList(1,2,3,4);
    value b = LazyList({1,2,3,4,5,6,7,8});
    check(LazyList({}).size==0, "empty LazyList()");
    //withLeading
    check({}.withLeading("A")=={"A"}.sequence, "Empty.withLeading(A)");
    check({}.withLeading("foo").size==1, "{}.withLeading.size");
    check(Array{}.withLeading(1)=={1}.sequence, "empty array.withLeading(1)");
    check(Array{1,2}.withLeading("A")=={"A",1,2}.sequence, "Array.withLeading(a)`` Array{1,2}.withLeading("A") ``");
    check([1,2].withLeading("a")=={"a",1,2}.sequence, "Sequence.withLeading(a)`` [1,2].withLeading("a") ``");
    check([1,2].withLeading("foo").size==3, "Sequence.withLeading.size`` [1,2].withLeading("foo").size ``");
    check(Singleton(1).withLeading("a")=={"a",1}.sequence, "Singleton.withLeading(a)`` Singleton(1).withLeading("a") ``");
    check((1..3).withLeading("a")=={"a",1,2,3}.sequence, "Range.withLeading(a)");
    check((1..3).withLeading(0).first==0, "Range.withLeading(a).first");
    check((1..3).withLeading(0).last==3, "Range.withLeading(a).last");
    check("abc".withLeading(1)=={1,'a','b','c'}.sequence, "String.withLeading(1)" +"abc".withLeading(1).string);
    check("".withLeading(1)=={1}.sequence, "\"\".withLeading(1)");
    check(b[100...]=={}, "LazyList[100...]");
    check(b[1...]==b.rest, "LazyList[1...]");

    //withTrailing
    check({}.withTrailing("A")=={"A"}.sequence, "Empty.withTrailing(A)");
    check({}.withTrailing("foo").size==1, "{}.withTrailing.size");
    check(Array{}.withTrailing(1)=={1}.sequence, "empty array.withTrailing(1)");
    check(Array{1,2}.withTrailing("A")=={1,2,"A"}.sequence, "Array.withTrailing(a)");
    check([1,2].withTrailing("a")=={1,2,"a"}.sequence, "Sequence.withTrailing(a)");
    check([1,2].withTrailing("foo").size==3, "Sequence.withTrailing.size");
    check(Singleton(1).withTrailing("a")=={1,"a"}.sequence, "Singleton.withTrailing(a)");
    check((1..3).withTrailing(4)=={1,2,3,4}.sequence, "Range.withTrailing(a)");
    check((1..3).withTrailing(4).first==1, "Range.withTrailing(a).first");
    check((1..3).withTrailing(4).last==4, "Range.withTrailing(a).last");
    check("abc".withTrailing(1)=={'a','b','c',1}.sequence, "String.withTrailing(1)");
    check("".withTrailing(1)=={1}.sequence, "\"\".withTrailing(1)");

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
    check(b.span(-2,2)=={1,2,3}.sequence, "LazyList.span(-2,2) & equals");
    check(b.span(2,-2)=={3,2,1}.sequence, "LazyList.span(2,-2) & equals");
    check(b.span(2,4)=={3,4,5}.sequence, "LazyList.span(2,4) & equals: `` b.span(2,4) `` instead of {3,4,5}");
    check(b.span(6,10)=={7,8}.sequence, "LazyList.span(6,10) & equals: `` b.span(6,10) `` instead of {7,8}");
    check(b.spanFrom(4)=={5,6,7,8}.sequence, "LazyList.spanFrom(4) & equals: `` b.spanFrom(4) `` instead of {5,6,7,8}");
    check(b.spanFrom(10)=={}, "LazyList.spanFrom(10) & equals");
    check(b.spanTo(4)=={1,2,3,4,5}.sequence, "LazyList.spanTo(4) & equals");
    check(b.spanTo(-1)=={}, "LazyList.spanTo(-1) & equals");
    check(b.segment(2,3)=={3,4,5}.sequence, "LazyList.segment: `` b.segment(2,3) `` instead of {3,4,5}");
    if (exists e=b.findLast((Integer x) => true)) {
        check(e==8, "LazyList.findLast");
    } else { fail("LazyList.findLast"); }
    if (exists e=b.first) {
        check(e==1, "LazyList.first");
    } else { fail("LazyList.last"); }
    if (exists e=b.last) {
        check(e==8, "LazyList.last");
    } else { fail("LazyList.last"); }
    check(b.reversed==ArraySequence(8..1), "LazyList.reversed");
    
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

    function trimmerFun(Integer i) => i<3 || i>5;
    check(b.trim(trimmerFun) == [3,4,5], "List.trim ``b.trim(trimmerFun)`` - expected [3,4,5]");
    check(b.trimLeading(trimmerFun) == [3,4,5,6,7,8], "List.trimLeading");
    check(b.trimTrailing(trimmerFun) == [1,2,3,4,5], "List.trimTrailing ``b.trimTrailing(trimmerFun)`` - expected [1,2,3,4,5]");
    check(b.trim((Integer i) => i>9) == b, "List.trim [2] differs from original: ``b.trim((Integer i) => i>9)``");
    check(b.trimLeading((Integer i) => i<0) == b, "List.trimLeading [2] differs from original: ``b.trimLeading((Integer i) => i<0)``");
    check(b.trimTrailing((Integer i) => i>9) == b, "List.trimTrailing [2] differs from original: ``b.trimTrailing((Integer i) => i>9)``");

    check(![ for (i in b) i.string ].startsWith(b), "List.occursAtStart [1]");
    check(![1,2,3,4,5,6,7].startsWith(b), "List.occursAtStart [2]");
    check([1,2,3,4,5,6,7,8,100,200,300].startsWith(b), "List.occursAtStart [3]");
    check([1,2,3,4,5,6,7,8].startsWith(b), "List.occursAtStart [4]");
    check(["bla", "ble", 1,2,3,4,5,6,7,8, "bli", "blo"].includes(b), "List.occursIn [1]");
    check([0,0,0,0,0,0,0,1,2,3,4,5,6,7,8].includes(b), "List.occursIn [2]");
    check(![0,0,0,0,1,2,3,4,5,6,7,0,8].includes(b), "List.occursIn [3]");
    check(![1,2,3,4,5,6,7].includes(b), "List.occursIn [4]");

    //#451
    check(b.indexesWhere((Integer i)=>i%2==0).sequence == [1,3,5,7], "List.indexesWhere");
    if (exists fi=b.firstIndexWhere((Integer i)=>i>5)) {
        check(fi == 5, "List.firstIndexWhere expected 5 got ``fi``");
    } else {
        fail("List.firstIndexWhere should exist");
    }
    if (exists li=b.lastIndexWhere((Integer i)=>i<5)) {
        check(li == 3, "List.lastIndexWhere expected 3 got ``li``");
    } else {
        fail("List.lastIndexWhere should exist");
    }
    check(b.indexesWhere(Integer.negative).empty, "List.indexesWhere empty");
    if (exists fi=b.firstIndexWhere(Integer.negative)) {
        fail("List.firstIndexWhere should not exist");
    }
    if (exists fi=b.lastIndexWhere(Integer.negative)) {
        fail("List.lastIndexWhere should not exist");
    }
    check("Probando Novedades".indexesWhere((Character c)=>c.uppercase).sequence == [0,9], "String.indexesWhere");
    if (exists fi="Probando".firstIndexWhere((Character c)=>c.lowercase)) {
        check(fi == 1, "String.firstIndexWhere expected 1 got ``fi``");
    } else {
        fail("String.firstIndexWhere not found");
    }
    if (exists li="TEst".lastIndexWhere((Character c)=>c.uppercase)) {
        check(li == 1, "String.lastIndexWhere expected 1 got ``li``");
    } else {
        fail("String.lastIndexWhere not found");
    }
    check("hello".indexesWhere(Character.whitespace).empty, "String.indexesWhere empty");
    if (exists fi="hello".firstIndexWhere(Character.whitespace)) {
        fail("String.firstIndexWhere should not exist");
    }
    if (exists fi="hello".lastIndexWhere(Character.whitespace)) {
        fail("String.lastIndexWhere should not exist");
    }
}

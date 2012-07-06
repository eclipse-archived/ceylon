void test_singleton() {
    String[] singleton = Singleton("hello");
    assert(singleton.size==1, "singleton size");
    assert(!singleton.empty, "singleton empty");
    assert(singleton.defines(0), "singleton defines");
    assert(!singleton.defines(1), "singleton defines");
    assert(singleton.string=="{ hello }", "singleton string");
    assert(singleton.reversed==singleton, "singleton reversed");
    assert(nonempty singleton, "singleton nonempty");
    if (nonempty singleton) {
        assert(singleton.first=="hello", "singleton first");
        assert(singleton.lastIndex==0, "sequence last index");
        assert(!nonempty singleton.rest, "singleton rest empty");
        if (nonempty rest = singleton.rest) {
            fail("singleton rest empty");
        }
    }
    else {
        fail("singleton nonempty");
    }
    variable value j:=0;
    for (x in singleton) {
        assert(x=="hello", "singleton iteration");
        j:=j+1;
    }
    assert(j==1, "singleton iteration");
    if (exists str=singleton[0]) {
        assert(str=="hello", "singleton item");
    }
    else {
        fail("singleton item");
    }
    if (exists str=singleton.item(0)) {
        assert(str=="hello", "singleton item");
    }
    else {
        fail("singleton item");
    }
    if (exists str=singleton[1]) {
        fail("singleton item");
    }
    if (exists str=singleton.item(1)) {
        fail("singleton item");
    }

    assert(nonempty singleton.span(0, 1), "nonempty singleton span(0,1)");
    assert(nonempty singleton.span(0, 0), "nonempty singleton span(0,0)");
    assert(nonempty singleton.span(0, null), "nonempty singleton span(0, null)");
    assert(nonempty singleton.segment(0, 1), "nonempty singleton segment(0,1)");
    assert(singleton.span(0, 3).string=="{ hello }", "singleton span(0,3).string");
    assert(singleton.segment(0, 3).string=="{ hello }", "singleton segment(0,3).string");
    assert(!nonempty singleton.span(1, 1), "!nonempty singleton span(1,1)");
    assert(!nonempty singleton.span(1, null), "!nonempty singleton span(1,null)");
    assert(!nonempty singleton.segment(1, 1), "!nonempty singleton segment(1,1)");
    assert(nonempty singleton.span(0, 0), "nonempty singleton span(0,0)");
    assert(nonempty singleton.span(0, 10), "nonempty singleton span(0,10)");
    assert(!nonempty singleton.segment(0, 0), "!nonempty singleton segment(0,0)");
    assert(!nonempty singleton.segment(0, -1), "!nonempty singleton segment(0,-1)");

    assert(singleton.keys.contains(0), "singleton keys.contains(0)");
    assert(!singleton.keys.contains(1), "!singleton keys.contains(1)");
    assert(!singleton.keys.contains(2), "!singleton keys.contains(2)");
    
    // Disabled: does not pass typechecker on M3.1
    //value ss = Singleton("Trompon").span(0, 0);
    //switch(ss)
    //case (is Empty) {}
    //case (is Singleton<String>) {
    //    String first = ss.first;
    //}
}

void test_join() {
    value l1 = { "join", 1,2,3};
    value l2 = { 4,5,6 };
    value l3 = {7,8,9};
    value joint = join(l1, l2, l3);
    assert(joint.size==l1.size+l2.size+l3.size, "join [1]");
    assert(join("aa", "bb", "cc").sequence=={`a`, `a`, `b`, `b`, `c`, `c`}, "join [2]");
}

void test_zip() {
    value keys = { 1, 2, 3, 4, 5, 6 };
    value items = { "one", "two", "three", "four", "five" };
    value z1 = zip(keys, items);
    value z2 = zip(keys, { "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete" });
    assert(z1.size==5, "zip");
    assert(z2.size==6, "zip");
}

//This is actually a test for the compiler. "exists" doesn't work yet.
void test_exists_nonempty() {
    String? yes = "yes";
    String? no = null;
    variable Integer[]? empties := Singleton(1);
    value t1 = exists yes then "yes exists" else "WTF";
    assert(t1 == "yes exists", "exists 1");
    value t2 = exists no then "WTF" else "no doesn't exist";
    assert(t2 == "no doesn't exist", "exists 2");
    value t3 = nonempty empties then "nonempty works" else "nonempty broken";
    assert(t3 == "nonempty works", "nonempty 1");
    Integer[] _t4 = {};
    value t4 = nonempty _t4 then "nonempty is broken" else "works";
    assert(t4 == "works", "nonempty 2");
}

void test_max_min() {
    Integer mx1 = max({1, 2, 3});
    assert(mx1==3, "max nonempty seq");
    Nothing mx2 = max({});
    Integer? mx3 = max(join({},{1, 2, 3}));
    assert((mx3 else 10)==3, "max joined seq");
    Integer? mx4 = max({1, 2, 3}.filter((Integer i) i>0));
    assert((mx4 else 10)==3, "max filtered seq");
    
    Integer mn1 = min({1, 2, 3});
    assert(mn1==1, "min nonempty seq");
    Nothing mn2 = min({});
    Integer? mn3 = min(join({},{1, 2, 3}));
    assert((mn3 else 10)==1, "min joined seq");
    Integer? mn4 = min({1, 2, 3}.filter((Integer i) i>0));
    assert((mn4 else 10)==1, "min filtered seq");
}

shared void sequences() {
    value builder = SequenceBuilder<String>();
    value empty = builder.sequence;
    assert(empty.size==0, "empty sequence");
    assert(!nonempty empty, "empty sequence");
    if (nonempty empty) {
        fail("empty sequence");
    }
    assert(!nonempty empty.span(1, 2), "empty.span(1,2)");
    assert(!nonempty empty.span(1, null), "empty.span(1,null)");
    assert(!nonempty empty.segment(1, 2), "empty sequence segment");
    assert(empty.string=="{}", "empty.string");
    assert(empty.reversed==empty, "empty reversed");
    assert(empty.sequence==empty, "empty.sequence");

    builder.append("hello");
    builder.append("world");
    value result = builder.sequence;
    assert(result.size==2, "sequence size");
    assert(nonempty result, "nonempty sequence");
    if (nonempty result) {
        assert(result.lastIndex==1, "sequence last index");
    }
    else {
        fail("sequence nonempty");
    }
    if (exists first = result.first) {
        assert(first=="hello", "sequence first");
    }
    else {
        fail("sequence first");
    }
    assert(result.sequence==result, "sequence.sequence");
    if (exists last = result.last) {
        assert(last=="world", "sequence last");
    }
    else {
        fail("sequence last");
    }
    assert(result.string=="{ hello, world }", "sequence.string 1");

    //span
    assert(result.span(1,1).string=="{ world }", "sequence.span(1,1).string");
    assert(result.span(1,null).string=="{ world }", "sequence.span(1,null).string");
    assert(result.span(0,3).string=="{ hello, world }", "sequence.span(0,3).string");
    //assert(result.span(1,0).string=="{ world, hello }", "sequence reverse span.string");
    assert(nonempty result.span(1,1), "nonempty sequence.span(1,1)");
    assert(nonempty result.span(0,0), "nonempty sequence.span(0,0)");

    //segment
    assert(result.segment(1,1).string=="{ world }", "sequence.segment(1,1).string");
    assert(result.segment(0,3).string=="{ hello, world }", "sequence.segment(0,3).string");
    assert(nonempty result.segment(1,1), "nonempty sequence.segment(1,1)");
    assert(!nonempty result.segment(0,0), "!nonempty sequence.segment(0,0)");
    //assert(!nonempty result.segment(1,-1), "!nonempty sequence.segment(1,-1)");
    
    assert (result.reversed=={"world", "hello"}, "sequence.reversed");

    if (exists str = result[0]) {
        assert(str=="hello", "sequence item");
    }
    else {
        fail("sequence item");
    }
    if (exists str = result[1]) {
        assert(str=="world", "sequence item");
    }
    else {
        fail("sequence item");
    }
    if (exists str = result[2]) {
        fail("sequence item");
    }

    assert(result.keys.contains(0), "sequence keys 0");
    assert(result.keys.contains(1), "sequence keys 1");
    assert(!result.keys.contains(2), "sequence keys 2");
    assert(result.defines(0)&&result.defines(1)&&!result.defines(2),
           "sequence defines");
    assert(result.definesEvery(0,1), "sequence definesEvery 0,1");
    assert(!result.definesEvery(1,2), "sequence definesEvery 1,2");
    assert(result.definesAny(1,2), "sequence definesAny 1,2");
    assert(!result.definesAny(2,3), "sequence definesAny 2,3");
    assert(result.items(0,1,2,3).string=="{ hello, world, null, null }", "sequence.items 1");
    assert(result.items(1,0).string=="{ world, hello }", "sequence.items 2");
    assert(result.items(5,6,7).string=="{ null, null, null }", "sequence.items 3");

    if (nonempty result) {
        value rest = result.rest;
        assert(rest.size==1, "rest size");
        assert(rest.keys.contains(0), "rest keys 1");
        assert(!rest.keys.contains(1), "rest keys 2");
        if (exists str = rest[0]) {
            assert(str=="world", "rest item");
        }
        else {
            fail("rest item");
        }
        if (exists str = rest[1]) {
            fail("rest item");
        }
        assert(nonempty rest, "empty rest");
        if (nonempty rest) {
            assert(rest.first=="world", "rest first");
            if (nonempty rr = rest.rest) {
                fail("rest rest");
            }
            assert(!nonempty rest.rest, "empty rest");
        }
        else {
            fail("rest nonempty");
        }
    }

    if (nonempty result) {
        value appender = SequenceAppender(result);
        appender.append("goodbye");
        value more = appender.sequence;
        assert(more.size==3, "sequence size");
        assert(more.first=="hello", "sequence first");
        assert(more.string=="{ hello, world, goodbye }", "sequence.string 2");
        appender.appendAll();
        appender.appendAll("everyone", "good luck!");
        //appender.append("everyone");
        //appender.append("good luck!");
        value evenMore = appender.sequence;
        assert(evenMore.size==5, "sequence size");
        assert(evenMore.string=="{ hello, world, goodbye, everyone, good luck! }", "sequence.string 3");
    }

    value seq = { 1, 2, 3, 4 };
    assert(seq.size==4, "sequence size");
    assert(seq.string=="{ 1, 2, 3, 4 }", "sequence.string 4");
    assert(seq.reversed=={4, 3, 2, 1}, "sequence reversed");
    assert(seq.first==1, "sequence first");
    assert(seq.rest.string=="{ 2, 3, 4 }", "sequence.rest.string");
    variable value i:=0;
    for (s in seq) {
        if (exists it=seq[i]) {
            assert(it==s, "sequence iteration");
        }
        else {
            fail("sequence iteration");
        }
        i:=i+1;
    }
    assert(i==4, "sequence iteration");

    value union = SequenceBuilder<String|Float>();
    union.append("x");
    union.append(5.1);
    union.appendAll("y", -1.2);
    value useq = union.sequence;
    assert(useq.size==4, "union sequence builder");
    assert(useq.string=="{ x, 5.1, y, -1.2 }", "union sequence builder.string");
    variable value s:=0;
    variable value f:=0;
    for (e in useq) {
        if (is String e) {
            s:=s+1;
        }
        if (is Float e) {
            f:=f+1;
        }
    }
    assert(s==2&&f==2, "union sequence iteration");

    test_singleton();

    value nulls = { null, "hello", null, "world" };
    if (exists n0 = nulls[0]) { fail("sequence with nulls"); }
    if (exists n1 = nulls[1]) {} else { fail("sequence with nulls"); }
    assert(nulls.string=="{ null, hello, null, world }", "sequence with nulls.string");
    variable value nonnull:=0;
    for (o in nulls) {
        if (exists o) { nonnull++; }
    }
    assert(nonnull==2, "iterate sequence with nulls");

    value coalesced = coalesce(nulls...).sequence;
    assert(coalesced.size==2, "coalesce size");
    assert(coalesced.string=="{ hello, world }", "coalesce.string");
    assert(coalesced.keys.contains(0), "coalesced keys");
    assert(coalesced.keys.contains(1), "coalesced keys");
    assert(!coalesced.keys.contains(2), "coalesced keys");
    assert(coalesced.defines(0)&&coalesced.defines(1)&&!coalesced.defines(2),
           "coalesce defines");
    assert(nonempty coalesced, "nonempty coalesced");
    value coal2 = coalesce(for (c in "hElLo") null).sequence;
    assert(!nonempty coal2, "nonempty coalesced2");
    assert(coal2.size == 0, "coalesced2.size");
    assert(!`h` in coal2, "coalesced2.contains");
    value entriesBuilder = SequenceBuilder<Integer->String>();
    entriesBuilder.append(1->"hello");
    entriesBuilder.append(2->"world");
    value entrySequence = entriesBuilder.sequence;
    assert(entrySequence.string=="{ 1->hello, 2->world }", "entries sequence.string");
    variable value cntr:=0;
    for (nat->str in entrySequence) {
        cntr++;
        assert(nat==1||nat==2, "entry key iteration");
        assert(str=="hello"||str=="world", "entry key iteration");
    }
    assert(cntr==2, "entry iteration");

    for (name->initial in { "Gavin"->`G`, "Tom"->`T` }) {
        assert(name.initial(1)==initial.string, "entry iteration");
    }

    value sequenceEntries = entries("X1", "X2", "X3");
    assert(sequenceEntries.sequence.size==3, "entries size");
    assert(nonempty sequenceEntries.sequence, "nonempty entries");
    if (exists primero=sequenceEntries.first) {
        assert(primero==Entry(0, "X1"), "entries first");
    }
    else {
        fail("entries first");
    }
    for (nat->str in sequenceEntries) {
        assert("X"+(nat+1).string==str, "entries iteration");
    }

    assert(append({},"foo").string=="{ foo }", "append to empty.string");
    assert(prepend({},"foo").string=="{ foo }", "prepend to empty.string");
    assert(append({1, 2},"foo").string=="{ 1, 2, foo }", "append.string");
    assert(prepend({1, 2},"foo").string=="{ foo, 1, 2 }", "prepend.string");

    assert(append({},"foo").size==1, "append to empty.size");
    assert(prepend({},"foo").size==1, "prepend to empty.size");
    assert(append({1, 2},"foo").size==3, "append.size");
    assert(prepend({1, 2},"foo").size==3, "prepend.size");
    assert(append({"one", "two" , "three"}, "four").size==4, "append");

    //More sequence-related functions
    test_join();
    test_zip();
    test_exists_nonempty();
    test_max_min();
    assert(nonempty emptyOrSingleton(1), "emptyOrSingleton [1]");
    assert(!nonempty emptyOrSingleton(null), "emptyOrSingleton [2]");
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
    assert((1..3).withTrailing("a","b")=={1,2,3,"a","b"}, "Range.withTrailing(a,b)");
    assert("abc".withTrailing()=="abc", "String.withTrailing()");
    assert("abc".withTrailing(1,2)=={`a`,`b`,`c`,1,2}, "String.withTrailing(1,2)");
    assert("".withTrailing()=="", "\"\".withTrailing()");
    assert("".withTrailing(1)=={1}, "\"\".withTrailing(1)");
}

void test_singleton() {
    String[] singleton = Singleton("hello");
    check(singleton.size==1, "singleton size");
    check(!singleton.empty, "singleton empty");
    check(singleton.defines(0), "singleton defines");
    check(!singleton.defines(1), "singleton defines");
    check(singleton.string=="{ hello }", "singleton string");
    check(singleton.reversed==singleton, "singleton reversed");
    check(nonempty singleton, "singleton nonempty");
    if (nonempty singleton) {
        check(singleton.first=="hello", "singleton first");
        check(singleton.lastIndex==0, "sequence last index");
        check(!nonempty singleton.rest, "singleton rest empty");
        if (nonempty rest = singleton.rest) {
            fail("singleton rest empty");
        }
        for (element in singleton.reversed) {
        }
        Sequence<String> s = singleton.sequence;
    }
    else {
        fail("singleton nonempty");
    }
    variable value j:=0;
    for (x in singleton) {
        check(x=="hello", "singleton iteration");
        j:=j+1;
    }
    check(j==1, "singleton iteration");
    if (exists str=singleton[0]) {
        check(str=="hello", "singleton item");
    }
    else {
        fail("singleton item");
    }
    if (exists str=singleton.item(0)) {
        check(str=="hello", "singleton item");
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

    check(nonempty singleton.span(0, 1), "nonempty singleton span(0,1)");
    check(nonempty singleton.span(0, 0), "nonempty singleton span(0,0)");
    check(nonempty singleton.spanFrom(0), "nonempty singleton spanFrom(0)");
    check(!nonempty singleton.spanFrom(1), "nonempty singleton spanFrom(1)");
    check(nonempty singleton.spanTo(0), "nonempty singleton spanTo(0)");
    check(nonempty singleton.spanTo(1), "nonempty singleton spanTo(1)");
    check(nonempty singleton.segment(0, 1), "nonempty singleton segment(0,1)");
    check(singleton.span(0, 3).string=="{ hello }", "singleton span(0,3).string");
    check(singleton.segment(0, 3).string=="{ hello }", "singleton segment(0,3).string");
    check(!nonempty singleton.span(1, 1), "!nonempty singleton span(1,1)");
    check(!nonempty singleton.spanFrom(1), "!nonempty singleton spanFrom(1)");
    check(!nonempty singleton.segment(1, 1), "!nonempty singleton segment(1,1)");
    check(nonempty singleton.span(0, 0), "nonempty singleton span(0,0)");
    check(nonempty singleton.span(0, 10), "nonempty singleton span(0,10)");
    check(!nonempty singleton.segment(0, 0), "!nonempty singleton segment(0,0)");
    check(!nonempty singleton.segment(0, -1), "!nonempty singleton segment(0,-1)");

    check(singleton.keys.contains(0), "singleton keys.contains(0)");
    check(!singleton.keys.contains(1), "!singleton keys.contains(1)");
    check(!singleton.keys.contains(2), "!singleton keys.contains(2)");
    
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
    check(joint.size==l1.size+l2.size+l3.size, "join [1]");
    check(join("aa", "bb", "cc").sequence=={`a`, `a`, `b`, `b`, `c`, `c`}, "join [2]");
}

void test_zip() {
    value keys = { 1, 2, 3, 4, 5, 6 };
    value items = { "one", "two", "three", "four", "five" };
    value z1 = zip(keys, items);
    value z2 = zip(keys, { "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete" });
    check(z1.size==5, "zip");
    check(z2.size==6, "zip");
}

//This is actually a test for the compiler. "exists" doesn't work yet.
void test_exists_nonempty() {
    String? yes = "yes";
    String? no = null;
    variable Integer[]? empties := Singleton(1);
    value t1 = exists yes then "yes exists" else "WTF";
    check(t1 == "yes exists", "exists 1");
    value t2 = exists no then "WTF" else "no doesn't exist";
    check(t2 == "no doesn't exist", "exists 2");
    value t3 = nonempty empties then "nonempty works" else "nonempty broken";
    check(t3 == "nonempty works", "nonempty 1");
    Integer[] _t4 = {};
    value t4 = nonempty _t4 then "nonempty is broken" else "works";
    check(t4 == "works", "nonempty 2");
}

void test_max_min() {
    Integer mx1 = max({1, 2, 3});
    check(mx1==3, "max nonempty seq");
    Nothing mx2 = max({});
    Integer? mx3 = max(join({},{1, 2, 3}));
    check((mx3 else 10)==3, "max joined seq");
    Integer? mx4 = max({1, 2, 3}.filter((Integer i) i>0));
    check((mx4 else 10)==3, "max filtered seq");
    
    Integer mn1 = min({1, 2, 3});
    check(mn1==1, "min nonempty seq");
    Nothing mn2 = min({});
    Integer? mn3 = min(join({},{1, 2, 3}));
    check((mn3 else 10)==1, "min joined seq");
    Integer? mn4 = min({1, 2, 3}.filter((Integer i) i>0));
    check((mn4 else 10)==1, "min filtered seq");
}

shared void sequences() {
    value builder = SequenceBuilder<String>();
    value empty = builder.sequence;
    check(empty.size==0, "empty sequence");
    check(!nonempty empty, "empty sequence");
    if (nonempty empty) {
        fail("empty sequence");
    }
    check(!nonempty empty.span(1, 2), "empty.span(1,2)");
    check(!nonempty empty.spanFrom(0), "empty.spanFrom(0)");
    check(!nonempty empty.spanTo(0), "empty.spanTo(0)");
    check(!nonempty empty.spanFrom(1), "empty.spanFrom(1)");
    check(!nonempty empty.spanTo(1), "empty.spanTo(1)");
    check(!nonempty empty.segment(1, 2), "empty sequence segment");
    check(empty.string=="{}", "empty.string");
    check(empty.reversed==empty, "empty reversed");
    check(empty.sequence==empty, "empty.sequence");

    builder.append("hello");
    builder.append("world");
    value result = builder.sequence;
    check(result.size==2, "sequence size");
    check(nonempty result, "nonempty sequence");
    if (nonempty result) {
        check(result.lastIndex==1, "sequence last index");
    }
    else {
        fail("sequence nonempty");
    }
    if (exists first = result.first) {
        check(first=="hello", "sequence first");
    }
    else {
        fail("sequence first");
    }
    check(result.sequence==result, "sequence.sequence");
    if (exists last = result.last) {
        check(last=="world", "sequence last");
    }
    else {
        fail("sequence last");
    }
    check(result.string=="{ hello, world }", "sequence.string 1");

    //span
    check(result.span(-2,-1).string=="{}", "sequence.span(-2,-1).string");
    check(result.span(-2,0).string=="{ hello }", "sequence.span(-2,0).string");
    check(result.span(1,1).string=="{ world }", "sequence.span(1,1).string");
    check(result.span(1,2).string=="{ world }", "sequence.span(1,2).string");
    check(result.spanFrom(1).string=="{ world }", "sequence.spanFrom(1).string");
    check(result.spanFrom(0).string=="{ hello, world }", "sequence.spanFrom(0).string");
    check(result.spanTo(1).string=="{ hello, world }", "sequence.spanTo(1).string");
    check(result.spanTo(0).string=="{ hello }", "sequence.spanTo(0).string");
    check(result.span(0,3).string=="{ hello, world }", "sequence.span(0,3).string");
    //check(result.span(1,0).string=="{ world, hello }", "sequence reverse span.string");
    check(nonempty result.span(1,1), "nonempty sequence.span(1,1)");
    check(nonempty result.span(0,0), "nonempty sequence.span(0,0)");

    //segment
    check(result.segment(1,1).string=="{ world }", "sequence.segment(1,1).string");
    check(result.segment(0,3).string=="{ hello, world }", "sequence.segment(0,3).string");
    check(nonempty result.segment(1,1), "nonempty sequence.segment(1,1)");
    check(!nonempty result.segment(0,0), "!nonempty sequence.segment(0,0)");
    //check(!nonempty result.segment(1,-1), "!nonempty sequence.segment(1,-1)");
    
    check(result.reversed=={"world", "hello"}, "sequence.reversed");

    if (exists str = result[0]) {
        check(str=="hello", "sequence item");
    }
    else {
        fail("sequence item");
    }
    if (exists str = result[1]) {
        check(str=="world", "sequence item");
    }
    else {
        fail("sequence item");
    }
    if (exists str = result[2]) {
        fail("sequence item");
    }

    check(result.keys.contains(0), "sequence keys 0");
    check(result.keys.contains(1), "sequence keys 1");
    check(!result.keys.contains(2), "sequence keys 2");
    check(result.defines(0)&&result.defines(1)&&!result.defines(2),
           "sequence defines");
    check(result.definesEvery(0,1), "sequence definesEvery 0,1");
    check(!result.definesEvery(1,2), "sequence definesEvery 1,2");
    check(result.definesAny(1,2), "sequence definesAny 1,2");
    check(!result.definesAny(2,3), "sequence definesAny 2,3");
    check(result.items(0,1,2,3).string=="{ hello, world, null, null }", "sequence.items 1");
    check(result.items(1,0).string=="{ world, hello }", "sequence.items 2");
    check(result.items(5,6,7).string=="{ null, null, null }", "sequence.items 3");

    if (nonempty result) {
        value rest = result.rest;
        check(rest.size==1, "rest size");
        check(rest.keys.contains(0), "rest keys 1");
        check(!rest.keys.contains(1), "rest keys 2");
        if (exists str = rest[0]) {
            check(str=="world", "rest item");
        }
        else {
            fail("rest item");
        }
        if (exists str = rest[1]) {
            fail("rest item");
        }
        check(nonempty rest, "empty rest");
        if (nonempty rest) {
            check(rest.first=="world", "rest first");
            if (nonempty rr = rest.rest) {
                fail("rest rest");
            }
            check(!nonempty rest.rest, "empty rest");
        }
        else {
            fail("rest nonempty");
        }
    }

    if (nonempty result) {
        value appender = SequenceAppender(result);
        appender.append("goodbye");
        value more = appender.sequence;
        check(more.size==3, "sequence size");
        check(more.first=="hello", "sequence first");
        check(more.string=="{ hello, world, goodbye }", "sequence.string 2");
        appender.appendAll();
        appender.appendAll("everyone", "good luck!");
        //appender.append("everyone");
        //appender.append("good luck!");
        value evenMore = appender.sequence;
        check(evenMore.size==5, "sequence size");
        check(evenMore.string=="{ hello, world, goodbye, everyone, good luck! }", "sequence.string 3");
    }

    value seq = { 1, 2, 3, 4 };
    check(seq.size==4, "sequence size");
    check(seq.string=="[ 1, 2, 3, 4 ]", "sequence.string 4: " + seq.string);
    check(seq.reversed=={4, 3, 2, 1}, "sequence reversed");
    check(seq.first==1, "sequence first");
    check(seq.rest.string=="[ 2, 3, 4 ]", "sequence.rest.string");
    variable value i:=0;
    for (s in seq) {
        if (exists it=seq[i]) {
            check(it==s, "sequence iteration");
        }
        else {
            fail("sequence iteration");
        }
        i:=i+1;
    }
    check(i==4, "sequence iteration");

    value union = SequenceBuilder<String|Float>();
    union.append("x");
    union.append(5.1);
    union.appendAll("y", -1.2);
    value useq = union.sequence;
    check(useq.size==4, "union sequence builder");
    check(useq.string=="{ x, 5.1, y, -1.2 }", "union sequence builder.string");
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
    check(s==2&&f==2, "union sequence iteration");

    test_singleton();

    Sequential<String?> nulls = { null, "hello", null, "world" };
    if (exists n0 = nulls[0]) { fail("sequence with nulls"); }
    if (exists n1 = nulls[1]) {} else { fail("sequence with nulls"); }
    check(nulls.string=="[ null, hello, null, world ]", "sequence with nulls.string");
    variable value nonnull:=0;
    for (o in nulls) {
        if (exists o) { nonnull++; }
    }
    check(nonnull==2, "iterate sequence with nulls");

    value coalesced = coalesce(nulls...).sequence;
    check(coalesced.size==2, "coalesce size");
    check(coalesced.string=="{ hello, world }", "coalesce.string");
    check(coalesced.keys.contains(0), "coalesced keys");
    check(coalesced.keys.contains(1), "coalesced keys");
    check(!coalesced.keys.contains(2), "coalesced keys");
    check(coalesced.defines(0)&&coalesced.defines(1)&&!coalesced.defines(2),
           "coalesce defines");
    check(nonempty coalesced, "nonempty coalesced");
    value coal2 = coalesce{for (c in "hElLo") null}.sequence;
    check(!nonempty coal2, "nonempty coalesced2");
    check(coal2.size == 0, "coalesced2.size");
    check(!`h` in coal2, "coalesced2.contains");
    value entriesBuilder = SequenceBuilder<Integer->String>();
    entriesBuilder.append(1->"hello");
    entriesBuilder.append(2->"world");
    value entrySequence = entriesBuilder.sequence;
    check(entrySequence.string=="{ 1->hello, 2->world }", "entries sequence.string");
    variable value cntr:=0;
    for (nat->str in entrySequence) {
        cntr++;
        check(nat==1||nat==2, "entry key iteration");
        check(str=="hello"||str=="world", "entry key iteration");
    }
    check(cntr==2, "entry iteration");

    for (name->initial in { "Gavin"->`G`, "Tom"->`T` }) {
        check(name.initial(1)==initial.string, "entry iteration");
    }

    value sequenceEntries = entries("X1", "X2", "X3");
    check(sequenceEntries.sequence.size==3, "entries size");
    check(nonempty sequenceEntries.sequence, "nonempty entries");
    if (exists primero=sequenceEntries.first) {
        check(primero==Entry(0, "X1"), "entries first");
    }
    else {
        fail("entries first");
    }
    for (nat->str in sequenceEntries) {
        check("X"+(nat+1).string==str, "entries iteration");
    }

    //More sequence-related functions
    test_join();
    test_zip();
    test_exists_nonempty();
    test_max_min();
    
    check(nonempty emptyOrSingleton(1), "emptyOrSingleton [1]");
    check(!nonempty emptyOrSingleton(null), "emptyOrSingleton [2]");
    
    check({"hello"}.withTrailing("world").first=="hello", "sequence with trailing");
    check({"world"}.withLeading("hello").first=="hello", "sequence with trailing");
    
    //collect
    check({ 1, 2, 3, 4, 5 }.collect((Integer i) i*2) == { 2, 4, 6, 8, 10 }, "Sequence<Integer>.collect");
    check("hola".collect((Character c) c.uppercased) == {`H`, `O`, `L`, `A`}, "Sequence<String>.collect");
    
    
}

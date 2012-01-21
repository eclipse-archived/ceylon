void test_singleton() {
    String[] singleton = Singleton("hello");
    assert(singleton.size==1, "singleton size");
    assert(!singleton.empty, "singleton empty");
    assert(singleton.defines(0), "singleton defines");
    assert(!singleton.defines(1), "singleton defines");
    assert(singleton.string=="{ hello }", "singleton string");
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
    assert(!nonempty singleton.segment(0, 1), "!nonempty singleton segment(0,1)");
    assert(nonempty singleton.segment(0, -1), "!nonempty singleton segment(0,-1)");
                                
    assert(singleton.keys.contains(0), "singleton keys.contains(0)");
    assert(!singleton.keys.contains(1), "!singleton keys.contains(1)");
    assert(!singleton.keys.contains(2), "!singleton keys.contains(2)");
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
    assert(result.string=="{ hello, world }", "sequence.string");
    assert(result.span(1,1).string=="{ world }", "sequence.span(1,1).string");
    assert(result.span(1,null).string=="{ world }", "sequence.span(1,null).string");
    assert(result.span(0,3).string=="{ hello, world }", "sequence.span(0,3).string");
    assert(result.segment(1,1).string=="{ world }", "sequence.segment(1,1).string");
    assert(result.segment(0,3).string=="{ hello, world }", "sequence.segment(0,3).string");
    assert(nonempty result.span(1,1), "nonempty sequence.span(1,1)");
    assert(nonempty result.segment(1,1), "nonempty sequence.segment(1,1)");
    assert(nonempty result.span(0,0), "nonempty sequence.span(0,0)");
    assert(!nonempty result.segment(0,0), "!nonempty sequence.segment(0,0)");

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

    assert(result.keys.contains(0), "sequence keys");
    assert(result.keys.contains(1), "sequence keys");
    assert(!result.keys.contains(2), "sequence keys");
    assert(result.defines(0)&&result.defines(1)&&!result.defines(2),
           "sequence defines");

    if (nonempty result) {
        value rest = result.rest;
        assert(rest.size==1, "rest size");
        assert(rest.keys.contains(0), "rest keys");
        assert(!rest.keys.contains(1), "rest keys");
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
        assert(more.string=="{ hello, world, goodbye }", "sequence.string");
        appender.appendAll();
        appender.appendAll("everyone", "good luck!");
        //appender.append("everyone");
        //appender.append("good luck!");
        value evenMore = appender.sequence;
        assert(evenMore.size==5, "sequence size");
        assert(evenMore.string=="{ hello, world, goodbye, everyone, good luck! }", "sequence.string");
    }
    
    value seq = { 1, 2, 3, 4 };
    assert(seq.size==4, "sequence size");
    assert(seq.string=="{ 1, 2, 3, 4 }", "sequence.string");
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
    union.append(5.0);
    union.appendAll("y", -1.0);
    value useq = union.sequence;
    assert(useq.size==4, "union sequence builder");
    assert(useq.string=="{ x, 5.0, y, -1.0 }", "union sequence builder.string");
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
    
    value coalesced = coalesce(nulls);
    assert(coalesced.size==2, "coalesce size");
    assert(coalesced.string=="{ hello, world }", "coalesce.string");
    assert(coalesced.keys.contains(0), "coalesced keys");
    assert(coalesced.keys.contains(1), "coalesced keys");
    assert(!coalesced.keys.contains(2), "coalesced keys");
    assert(coalesced.defines(0)&&coalesced.defines(1)&&!coalesced.defines(2),
           "coalesce defines");
    assert(nonempty coalesced, "nonempty coalesced");
    
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
    assert(sequenceEntries.size==3, "entries size");
    assert(nonempty sequenceEntries, "nonempty entries");
    if (nonempty sequenceEntries) {
        assert(sequenceEntries.first==Entry(0, "X1"), "entries first");
    }
    else {
        fail("entries empty");
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
    
}

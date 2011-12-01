shared void sequences() {
    value builder = SequenceBuilder<String>();
    value empty = builder.sequence;
    assert(empty.size==0, "empty sequence");
    if (nonempty empty) {
        fail("empty sequence");
    }
    assert(empty.string=="{}", "empty sequence string");
    builder.append("hello");
    builder.append("world");
    value result = builder.sequence;
    assert(result.size==2, "sequence size");
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
    assert(result.string=="{ hello, world }", "sequence string");

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

    //assert(result.keys.contains(0), "sequence keys");
    //assert(result.keys.contains(1), "sequence keys");
    //assert(!result.keys.contains(2), "sequence keys");

    if (nonempty result) {
        value appender = SequenceAppender(result);
        appender.append("goodbye");
        value more = appender.sequence;
        assert(more.size==3, "sequence size");
        assert(more.first=="hello", "sequence first");
        assert(more.string=="{ hello, world, goodbye }", "sequence string");
        appender.appendAll();
        appender.appendAll("everyone", "good luck!");
        //appender.append("everyone");
        //appender.append("good luck!");
        value evenMore = appender.sequence;
        assert(evenMore.size==5, "sequence size");
        assert(evenMore.string=="{ hello, world, goodbye, everyone, good luck! }", "sequence string");
    }
    
    value seq = { 1, 2, 3, 4 };
    assert(seq.size==4, "sequence size");
    assert(seq.string=="{ 1, 2, 3, 4 }", "sequence string");
    assert(seq.first==1, "sequence first");
    assert(seq.rest.string=="{ 2, 3, 4 }", "sequence rest string");
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
    assert(useq.string=="{ x, 5.0, y, -1.0 }", "union sequence builder");
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
    
    String[] singleton = Singleton("hello");
    assert(singleton.size==1, "singleton size");
    assert(!singleton.empty, "singleton empty");
    assert(singleton.defines(0), "singleton defines");
    assert(!singleton.defines(1), "singleton defines");
    assert(singleton.string=="{ hello }", "singletone string");
    if (nonempty singleton) {
        assert(singleton.first=="hello", "singleton first");
        assert(singleton.lastIndex==0, "sequence last index");
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
    if (exists str=singleton[1]) {
        fail("singleton item");
    }
    
    assert(singleton.keys.contains(0), "singleton keys");
    assert(!singleton.keys.contains(1), "singleton keys");
    assert(!singleton.keys.contains(2), "singleton keys");

}

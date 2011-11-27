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
    if (exists first = result.first) {
        assert(first=="hello", "sequence first");
    }
    else {
        fail("sequence first");
    }
    assert(result.string=="{ hello, world }", "sequence string");
    if (nonempty result) {
        value appender = SequenceAppender(result);
        appender.append("goodbye");
        value more = appender.sequence;
        assert(more.size==3, "sequence size");
        assert(more.first=="hello", "sequence first");
        assert(more.string=="{ hello, world, goodbye }", "sequence string");
        appender.appendAll("everyone", "good luck!");
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
        i+=1;
    }
    assert(i==4, "sequence iteration");
}
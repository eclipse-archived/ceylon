shared void sequences() {
    value builder = SequenceBuilder<String>();
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
    print(result);
    if (nonempty result) {
        value appender = SequenceAppender(result);
        appender.append("goodbye");
        value more = appender.sequence;
        assert(more.size==3, "sequence size");
        assert(more.first=="hello", "sequence first");
        print(more);
        appender.appendAll("everyone", "good luck!");
        value evenMore = appender.sequence;
        assert(evenMore.size==5, "sequence size");
        print(evenMore);
    }
}
import ceylon.language.meta.model { IncompatibleTypeException }

@test
shared void bug691() {
    // subtype is fine
    assert(`Integer`.getAttribute<Nothing,Boolean,Nothing>("unit") exists);
    // supertype does not have this attribute
    assert(!`Integer`.getAttribute<Anything,Boolean,Nothing>("unit") exists);
    // exact type is fine
    assert(`Integer`.getAttribute<Integer,Boolean,Nothing>("unit") exists);
    // common supertype does not have this attribute
    assert(!`Integer`.getAttribute<String,Boolean,Nothing>("unit") exists);

    // supertype has this attribute
    assert(`Integer`.getAttribute<Object,Integer,Nothing>("hash") exists);
    // common supertype is Object, so it has this attribute
    assert(`Integer`.getAttribute<String,Integer,Nothing>("hash") exists);
}

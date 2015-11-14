import ceylon.language.meta.model { Method }

shared class Bug548() {
    shared variable String? opt="";
    shared String? setOpt(String s) => "";
}

@test
shared void bug548() {
    Method<Bug548, Anything, [String]>? m = `Bug548.setOpt`; // m is null
    assert(exists m);
}

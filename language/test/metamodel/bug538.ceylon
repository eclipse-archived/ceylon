import ceylon.language.meta { type, modules }

@test
shared void bug538() {
    Anything n = `null`.get();
    print(n);
    assert(!n exists);
}

class Bug647() {
    shared class Member() {}
}
@test
shared void bug647() {
    Object inst = `class Bug647`.instantiate([]);
    Object mem = `class Bug647.Member`.memberInstantiate(inst);
}
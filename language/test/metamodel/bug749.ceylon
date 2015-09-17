shared class Bug749 {
    shared new mmBarValue {}
}
@test
shared void bug749() {
    print(`Bug749`.getCallableConstructors<>());
}
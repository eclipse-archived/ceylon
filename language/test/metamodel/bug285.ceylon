class Bug285(){
    shared class Inner(){}
    shared void method(){}
    shared Integer attr = 2;
}

@test
shared void bug285() {
    value b = `Bug285`;
    assert(!b.getAttribute("Inner") exists);
    assert(!b.getAttribute("method") exists);
    assert(!b.getMethod("Inner") exists);
    assert(!b.getMethod("attr") exists);
    assert(!b.getClassOrInterface("method") exists);
    assert(!b.getClassOrInterface("attr") exists);
}
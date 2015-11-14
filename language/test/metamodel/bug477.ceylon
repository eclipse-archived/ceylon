class Bug477TopLevel(a, b, c) {
    shared Integer a;
    shared Integer b;
    shared Integer c;
}

@test
shared void bug477() {
    class Inner(a, b, c) {
        shared Integer a;
        shared Integer b;
        shared Integer c;
    }
    
    assert(`class Bug477TopLevel`.parameterDeclarations ==
            [`value Bug477TopLevel.a`, `value Bug477TopLevel.b`, `value Bug477TopLevel.c`]);
    assert(`class Inner`.parameterDeclarations ==
            // missing Inner.a here
            [`value Inner.a`, `value Inner.b`, `value Inner.c`]);
}

import m2 { ... }

shared class TestClass3() extends Test2() {
    assert(2+2==4);
}

shared class TestClass4(String name) {
    shared actual String string { return name; }
}

shared class SelfClass() satisfies WithSelfType<SelfClass> {
    shared actual void somethingWith(SelfClass me) { print(me); }
}

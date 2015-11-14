import m2 { ... }

shared class TestClass3() extends Test2() {
    assert(2+2==4);
    variable value i=0;
    i++;
    Integer[] seq = [1,2,3];
    if (nonempty seq) {}
}

shared class TestClass4(String name) {
    defmethod(name);
    defmethod();
    shared actual String string { return name; }
}

shared class SelfClass() satisfies WithSelfType<SelfClass> {
    shared actual void somethingWith(SelfClass me) { print(me); }
}

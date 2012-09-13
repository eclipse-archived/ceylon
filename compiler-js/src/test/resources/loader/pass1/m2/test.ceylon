import m1 { ... }

shared class Test2() extends TestClass1() {
    test1();
}

shared interface WithSelfType<Myself> of Myself
        given Myself satisfies WithSelfType<Myself> {
    shared formal void somethingWith(Myself me);
}

shared void defmethod(String p="default value") {}

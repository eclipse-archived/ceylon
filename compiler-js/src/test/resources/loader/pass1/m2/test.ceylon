import m1 { ... }

shared class Test2() extends TestClass1() {
    test1();
}

shared interface WithSelfType<Myself> of Myself
        given Myself satisfies WithSelfType<Myself> {
    shared formal void somethingWith(Myself me);
}

shared void defmethod(String p="default value") {}

//Test nested types
shared class Nested2() extends TestNestedClass1() {
    shared class TestNested2() extends TestNested1() {}
    shared TestNested1 foo() => TestNested2();
}

shared class Nested3() extends TestNestedInterface1() {
    shared actual TestNested1 foo() {
        object n satisfies TestNested1 {}
        return n;
    }
}

shared class Nested4() extends TestNestedObject1() {
    shared Singleton<Character> foo() => character();
}

shared class Nested5() satisfies NestedIface1 {
    shared actual TestNested1 foo() => TestNested1();
}

shared class Nested6() satisfies NestedIface2 {
    shared actual TestNested2 foo() {
        object n satisfies TestNested2{}
        return n;
    }
}

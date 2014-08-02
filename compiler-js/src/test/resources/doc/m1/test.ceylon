shared void test1() {}

shared interface TestInterface1 {}

shared class TestClass1() satisfies TestInterface1 {
}

shared class TestNestedClass1() {
    shared class TestNested1() {}
}

shared abstract class TestNestedInterface1() {
    shared interface TestNested1 {}
    shared formal TestNested1 foo();
}

shared class TestNestedObject1() {
    object char extends Singleton<Character>('c') {}
    shared Singleton<Character> character() => char;
}

shared interface NestedIface1 {
    shared class TestNested1() {}
    shared formal TestNested1 foo();
}

shared interface NestedIface2 {
    shared interface TestNested2 {}
    shared formal TestNested2 foo();
}

variable Integer assertionCount:=0;
variable Integer failureCount:=0;

shared void assert(Boolean assertion, String message="") {
    assertionCount+=1;
    if (!assertion) {
        failureCount+=1;
        print("assertion failed \"" message "\"");
    }
}

shared void fail(String message) {
    assert(false, message);
}

shared void results() {
    print("assertions " assertionCount 
          ", failures " failureCount "");
}


shared void helloWorld() {
    print("hello world");
}

shared void hello(String name) {
    print("hello" + name);
}

shared void helloAll(String... names) {}

shared String toString(Object obj) {
    return obj.string;
}

shared Float add(Float x, Float y) {
    return x+y;
}

shared void repeat(Integer times, void f(Integer iter)) {
    f(0);
}

class MySequence<out Element>(Sequence<Element> seq)
            satisfies Sequence<Element> {
    shared actual Integer lastIndex { return seq.lastIndex; }
    shared actual Element first { return seq.first; }
    shared actual Element[] rest { return seq.rest; }
    shared actual Element? item(Integer index) { return seq[index]; }
    shared actual Element[] span(Integer from, Integer? to) { return seq.span(from, to); }
    shared actual Element[] segment(Integer from, Integer length) { return seq.segment(from, length); }
    shared actual MySequence<Element> clone { return this; }
    shared actual String string { return seq.string; }
}

class RefHelper() {
    shared Boolean f(Integer i) { return true; }
}

void testMethodReference() {
    value obj1 = RefHelper();
    value obj2 = MySequence<String>({"hi"});
    Boolean tst(Boolean x(Integer i)) {
        return x(0);
    }
    assert(tst(obj1.f), "Reference to method");
    assert(tst(obj2.defines), "Reference to method from ceylon.language");
}

shared void test() {
    helloWorld();
    hello("test");
    helloAll("Gavin", "Enrique", "Ivo");
    assert(toString(5)=="5", "toString(obj)");
    assert(add(1.5, 2.5)==4.0,"add(Float,Float)");
    //repeat(5, void p(Integer x) { print(x); });
    testMethodReference();
    results();
}

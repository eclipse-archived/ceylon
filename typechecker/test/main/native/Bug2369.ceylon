native class Bug2369_1() {
    shared native String foo() => "foo";
    shared native class Bug2369Inner() {
        shared String foo() => "fooInner";
    }
}

native("jvm") class Bug2369_1() {
    shared native("jvm") class Bug2369Inner() {}
}

native class Bug2369_2() {
    shared native String foo() => "foo";
    shared native class Bug2369_2() {
        shared String foo() => "fooInner";
    }
}

native("jvm") class Bug2369_2() {
    shared native("jvm") class Bug2369_2() {}
}

shared void testBug2369() {
    if (Bug2369_1().foo() == "foo" &&
            Bug2369_1().Bug2369Inner().foo() == "fooInner" &&
            Bug2369_2().foo() == "foo" &&
            Bug2369_2().Bug2369_2().foo() == "fooInner"){
        throw Exception("Bug2369-JVM");
    }
}

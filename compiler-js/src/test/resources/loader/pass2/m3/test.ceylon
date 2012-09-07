import m2 { ... }

shared class TestClass3() extends Test2() {
}

shared class TestClass4(String name) {
    shared actual String string { return name; }
}

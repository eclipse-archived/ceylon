native shared class NativeScopes() {
    native shared Integer foo() {
        return 1;
    }
    native shared void test();
}

native("jvm") shared class NativeScopes() {
    native("jvm") shared void test() {
        native("jvm") void test2() {
            native("jvm") void test3() {
                value x = foo();
            }
        }
        native("jvm") class Bar() {
            native("jvm") class Bar2() {
                native("jvm") void test() {
                    value x = foo();
                }
            }
        }
        throw Exception("NativeScopes-JVM");
    }
}

native("js") shared class NativeScopes() {
    native("js") shared void test() {
        native("js") void test2() {
            native("js") void test3() {
                value x = foo();
            }
        }
        native("js") class Bar() {
            native("js") class Bar2() {
                native("js") void test() {
                    value x = foo();
                }
            }
        }
        throw Exception("NativeScopes-JS");
    }
}

void testNativeScopes() {
    NativeScopes().test();
}

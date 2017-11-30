native class NativeClassPrivate() {
    native shared Integer test(Integer i);
    native shared Integer test2(Integer i) => i;
    native Integer test3(Integer i);
    native Integer test4(Integer i) => i;
    native shared Integer foo;
    native shared Integer bar;
    native assign bar;
}

native("jvm") class NativeClassPrivate() {
    native("jvm") shared Integer test(Integer i) {
        throw Exception("NativeClassPrivate-JVM");
    }
    native("jvm") Integer test3(Integer i) => i;
    native("jvm") shared Integer foo => 0;
    native("jvm") shared Integer bar => 0;
    native("jvm") assign bar { test(test2(test3(test4(0)))); }
}

native("js") class NativeClassPrivate() {
    native("js") shared Integer test(Integer i) {
        throw Exception("NativeClassPrivate-JS");
    }
    native("js") Integer test3(Integer i) => i;
    native("js") shared Integer foo => 0;
    native("js") shared Integer bar => 0;
    native("js") assign bar { test(test2(test3(test4(0)))); }
}

void testNativeClassPrivate() {
    value x = NativeClassPrivate().foo;
    value y = NativeClassPrivate().bar;
    NativeClassPrivate().bar = x;
}

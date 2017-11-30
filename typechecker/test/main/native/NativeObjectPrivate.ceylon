native object nativeObjectPrivate {
    native shared Integer test(Integer i);
    native shared Integer foo;
    native shared Integer bar;
    native assign bar;
}

native("jvm") object nativeObjectPrivate {
    native("jvm") shared Integer test(Integer i) {
        throw Exception("NativeObjectPrivate-JVM");
    }
    native("jvm") shared Integer foo => 0;
    native("jvm") shared Integer bar => 0;
    native("jvm") assign bar { test(0); }
}

native("js") object nativeObjectPrivate {
    native("js") shared Integer test(Integer i) {
        throw Exception("NativeObjectPrivate-JS");
    }
    native("js") shared Integer foo => 0;
    native("js") shared Integer bar => 0;
    native("js") assign bar {test(0); }
}

void testNativeObjectPrivate() {
    value x = nativeObjectPrivate.foo;
    value y = nativeObjectPrivate.bar;
    nativeObjectPrivate.bar = x;
}

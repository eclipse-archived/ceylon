native shared object nativeObjectShared {
    native shared Integer test(Integer i);
    native shared Integer foo;
    native shared Integer bar;
    native assign bar;
}

native("jvm") shared object nativeObjectShared {
    native("jvm") shared Integer test(Integer i) {
        throw Exception("NativeObjectShared-JVM");
    }
    native("jvm") shared Integer foo => 0;
    native("jvm") shared Integer bar => 0;
    native("jvm") assign bar { test(0); }
}

native("js") shared object nativeObjectShared {
    native("js") shared Integer test(Integer i) {
        throw Exception("NativeObjectShared-JS");
    }
    native("js") shared Integer foo => 0;
    native("js") shared Integer bar => 0;
    native("js") assign bar {test(0); }
}

void testNativeObjectShared() {
    value x = nativeObjectShared.foo;
    value y = nativeObjectShared.bar;
    nativeObjectShared.bar = x;
}

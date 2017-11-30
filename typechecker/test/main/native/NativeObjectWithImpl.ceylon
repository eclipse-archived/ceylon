native shared object nativeObjectWithImpl {
    native shared Integer test(Integer i) {
        throw Exception("NativeObjectWithImpl-JVM");
    }
    native shared Integer foo => 0;
    native shared Integer bar => 0;
    native assign bar { test(0); }
}

native("jvm") shared object nativeObjectWithImpl {
}

native("js") shared object nativeObjectWithImpl {
    native("js") shared Integer test(Integer i) {
        throw Exception("NativeObjectWithImpl-JS");
    }
    native("js") shared Integer foo => 0;
    native("js") shared Integer bar => 0;
    native("js") assign bar {test(0); }
}

void testNativeObjectWithImpl() {
    value x = nativeObjectWithImpl.foo;
    value y = nativeObjectWithImpl.bar;
    nativeObjectWithImpl.bar = x;
}

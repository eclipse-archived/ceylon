native interface NativeInterfacePrivate {
    native shared Integer test(Integer i);
    native shared Integer test2(Integer i) => i;
    native Integer test3(Integer i);
    native Integer test4(Integer i) => i;
    native shared Integer foo;
    native shared Integer bar;
    native assign bar;
}

native("jvm") interface NativeInterfacePrivate {
    native("jvm") shared Integer test(Integer i) {
        throw Exception("NativeInterfacePrivate-JVM");
    }
    native("jvm") Integer test3(Integer i) => i;
    native("jvm") shared Integer foo => 0;
    native("jvm") shared Integer bar => 0;
    native("jvm") assign bar { test(test2(test3(test4(0)))); }
}

native("js") interface NativeInterfacePrivate {
    native("js") shared Integer test(Integer i) {
        throw Exception("NativeInterfacePrivate-JS");
    }
    native("js") Integer test3(Integer i) => i;
    native("js") shared Integer foo => 0;
    native("js") shared Integer bar => 0;
    native("js") assign bar { test(test2(test3(test4(0)))); }
}

class NativeInterfacePrivateImpl() satisfies NativeInterfacePrivate {
}

void testNativeInterfacePrivate() {
    value x = NativeInterfacePrivateImpl().foo;
    value y = NativeInterfacePrivateImpl().bar;
    NativeInterfacePrivateImpl().bar = x;
}

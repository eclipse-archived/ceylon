native shared interface NativeInterfaceShared {
    native shared Integer test(Integer i);
    native shared Integer foo;
    native shared Integer bar;
    native assign bar;
    shared void access() {
        test(foo + bar);
    }
    native shared class Inner() {
        native shared Integer baz();
    }
    native shared class Inner2() {
        native shared Integer baz() => 1;
    }
    native shared interface IInner {
        native shared Integer baz();
    }
    native shared interface IInner2 {
        native shared Integer baz() => 1;
    }
}

native("jvm") shared interface NativeInterfaceShared {
    native("jvm") shared Integer test(Integer i) {
        throw Exception("NativeInterfaceShared-JVM");
    }
    native("jvm") shared Integer foo => 0;
    native("jvm") shared Integer bar => 0;
    native("jvm") assign bar { test(0); }
    native("jvm") shared class Inner() {
        native("jvm") shared Integer baz() => 1;
    }
    native("jvm") shared interface IInner {
        native("jvm") shared Integer baz() => 1;
    }
}

native("js") shared interface NativeInterfaceShared {
    native("js") shared Integer test(Integer i) {
        throw Exception("NativeInterfaceShared-JS");
    }
    native("js") shared Integer foo => 0;
    native("js") shared Integer bar => 0;
    native("js") assign bar {test(0); }
    native("js") shared class Inner() {
        native("js") shared Integer baz() => 1;
    }
    native("js") shared interface IInner {
        native("js") shared Integer baz() => 1;
    }
}

class NativeInterfaceSharedImpl() satisfies NativeInterfaceShared {
}

void testNativeInterfaceShared() {
    value x = NativeInterfaceSharedImpl();
    value a = x.foo;
    value b = x.bar;
    x.bar = b;
    x.access();
    value y = x.Inner();
    value d = y.baz();
    value z = x.Inner2();
}

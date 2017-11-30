
abstract class NativeClassExtendsSuper<A,B>(shared A a, B b) {
    shared formal A test();
    shared B test2() { return b; }
}

native class NativeClassExtends<A,B,C>(A a, B b, C c) extends NativeClassExtendsSuper<A,B>(a, b) {
    native shared actual A test();
    native shared C test3() { return c; }
    native shared Integer test4() { return 1; }
}

native("jvm") class NativeClassExtends<A,B,C>(A a, B b, C c) extends NativeClassExtendsSuper<A,B>(a, b) {
    native("jvm") shared actual A test() {
        throw Exception("NativeClassExtends-JVM");
    }
    native("jvm") shared C test3() { return c; }
}

native("js") class NativeClassExtends<A,B,C>(A a, B b, C c) extends NativeClassExtendsSuper<A,B>(a, b) {
    native("js") shared actual A test() {
        throw Exception("NativeClassExtends-JS");
    }
}

shared void testNativeClassExtends() {
    value obj = NativeClassExtends<Integer,String,Boolean>(1, "foo", true);
    value a = obj.test();
    value b = obj.test2();
    value c = obj.test3();
    value d = obj.test4();
}
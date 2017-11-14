
shared interface INativeDelegate {
    shared formal void test();
}

native("jvm")
class NativeDelegateJvm() satisfies INativeDelegate {
    shared actual void test() {
        throw Exception("NativeDelegate-JVM");
    }
}

native("js")
class NativeDelegateJs() satisfies INativeDelegate {
    shared actual void test() {
        throw Exception("NativeDelegate-JS");
    }
}

native INativeDelegate createNativeDelegate();
native("jvm") INativeDelegate createNativeDelegate() => NativeDelegateJvm();
native("js") INativeDelegate createNativeDelegate() => NativeDelegateJs();

shared void testNativeDelegate() {
    createNativeDelegate().test();
}


interface NativeClassSatisfiesSuper1 {
    shared formal void test1(Integer i);
}
interface NativeClassSatisfiesSuper2 {
    shared formal void test2(Integer i);
}

native class NativeClassSatisfies() satisfies NativeClassSatisfiesSuper1 & NativeClassSatisfiesSuper2 {
    native shared actual void test1(Integer i);
    native shared actual void test2(Integer i);
}

native("jvm") class NativeClassSatisfies() satisfies NativeClassSatisfiesSuper1 & NativeClassSatisfiesSuper2 {
    native("jvm") shared actual void test1(Integer i) {
        throw Exception("NativeClassSatisfies-JVM");
    }
    native("jvm") shared actual void test2(Integer i) {
        test1(i);
    }
}

native("js") class NativeClassSatisfies() satisfies NativeClassSatisfiesSuper1 & NativeClassSatisfiesSuper2 {
    native("js") shared actual void test1(Integer i) {
        throw Exception("NativeClassSatisfies-JS");
    }
    native("js") shared actual void test2(Integer i) {
        test1(i);
    }
}

shared void testNativeClassSatisfies() {
    value x = NativeClassSatisfies().test2(0);
}
native class NativeCorrectOrder() {
    native shared variable Integer i = 0;
    native shared Integer j = ++i;
    native shared Integer k = ++i;
}

native("jvm") class NativeCorrectOrder() {
    native("jvm") shared variable Integer i = 10;
}

native("js") class NativeCorrectOrder() {
    native("js") shared variable Integer i = 10;
}

shared void testNativeCorrectOrder() {
    value x = NativeCorrectOrder();
    if (x.i == 12 && x.j == 11 && x.k == 12) {
        throw Exception("NativeCorrectOrder-JVM");
    }
}
native shared void nativeMethodHeaderImpl() {
    throw Exception("NativeMethodHeaderImpl-JVM");
}

native("js") shared void nativeMethodHeaderImpl() {
    throw Exception("NativeMethodHeaderImpl-JS");
}

void testNativeMethodHeaderImpl() {
    nativeMethodHeaderImpl();
}

native shared void nativeMethodShared();

native("jvm") shared void nativeMethodShared() {
    throw Exception("NativeMethodShared-JVM");
}

native("js") shared void nativeMethodShared() {
    throw Exception("NativeMethodShared-JS");
}

void testNativeMethodShared() {
    nativeMethodShared();
}

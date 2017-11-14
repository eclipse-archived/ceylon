native void nativeMethodPrivate();
native("jvm") void nativeMethodPrivate() {
    throw Exception("NativeMethodPrivate-JVM");
}

native("js") void nativeMethodPrivate() {
    throw Exception("NativeMethodPrivate-JS");
}

void testNativeMethodPrivate() {
    nativeMethodPrivate();
}

native void nativeMethodPrivateAbstraction();

native("jvm") void nativeMethodPrivateAbstraction() {
    throw Exception("NativeMethodPrivateAbstraction-JVM");
}

native("js") void nativeMethodPrivateAbstraction() {
    throw Exception("NativeMethodPrivateAbstraction-JS");
}

void testNativeMethodPrivateAbstraction() {
    nativeMethodPrivateAbstraction();
}

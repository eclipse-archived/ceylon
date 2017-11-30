void testNativeMethodLocal() {
    native void nativeMethodLocal();
    native("jvm") void nativeMethodLocal() {
        throw Exception("NativeMethodLocal-JVM");
    }
    native("js") void nativeMethodLocal() {
        throw Exception("NativeMethodLocal-JS");
    }
    
    nativeMethodLocal();
}

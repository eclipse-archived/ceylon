native shared String nativeAttributeHeaderImpl {
    throw Exception("NativeAttributeHeaderImpl-JVM");
}

native("js") shared String nativeAttributeHeaderImpl {
    throw Exception("NativeAttributeHeaderImpl-JS");
}

void testNativeAttributeHeaderImpl() {
    value x = nativeAttributeHeaderImpl;
}

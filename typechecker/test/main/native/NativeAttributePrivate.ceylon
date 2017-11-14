native Integer nativeAttributePrivate1;
native("jvm") Integer nativeAttributePrivate1 = 1;
native("js") Integer nativeAttributePrivate1 = 2;

native variable Integer nativeAttributePrivate2;
native("jvm") variable Integer nativeAttributePrivate2 = 1;
native("js") variable Integer nativeAttributePrivate2 = 2;

native Integer nativeAttributePrivate3;
native("jvm") Integer nativeAttributePrivate3 {
    throw Exception("NativeAttributePrivate-JVM");
}
native("js") Integer nativeAttributePrivate3 {
    throw Exception("NativeAttributePrivate-JS");
}


void testNativeAttributePrivate() {
    value x = nativeAttributePrivate1;
    value y = nativeAttributePrivate2;
    nativeAttributePrivate2 = 3;
    value z = nativeAttributePrivate3;
}

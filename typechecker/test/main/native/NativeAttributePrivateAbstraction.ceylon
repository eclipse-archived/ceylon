native Integer nativeAttributePrivateAbstraction1;

native("jvm") Integer nativeAttributePrivateAbstraction1 = 1;

native("js") Integer nativeAttributePrivateAbstraction1 = 2;


native variable Integer nativeAttributePrivateAbstraction2;

native("jvm") variable Integer nativeAttributePrivateAbstraction2 = 1;

native("js") variable Integer nativeAttributePrivateAbstraction2 = 2;


native Integer nativeAttributePrivateAbstraction3;

native("jvm") Integer nativeAttributePrivateAbstraction3 {
    throw Exception("NativeAttributePrivateAbstraction-JVM");
}

native("js") Integer nativeAttributePrivateAbstraction3 {
    throw Exception("NativeAttributePrivateAbstraction-JS");
}


void testNativeAttributePrivateAbstraction() {
    value x = nativeAttributePrivateAbstraction1;
    value y = nativeAttributePrivateAbstraction2;
    nativeAttributePrivateAbstraction2 = 3;
    value z = nativeAttributePrivateAbstraction3;
}

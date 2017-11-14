void testNativeAttributeLocal() {
    native Integer nativeAttributeLocal1;
    native("jvm") Integer nativeAttributeLocal1 = 1;
    native("js") Integer nativeAttributeLocal1 = 2;
    
    native variable Integer nativeAttributeLocal2;
    native("jvm") variable Integer nativeAttributeLocal2 = 1;
    native("js") variable Integer nativeAttributeLocal2 = 2;
    
    native Integer nativeAttributeLocal3;
    native("jvm") Integer nativeAttributeLocal3 {
        throw Exception("NativeAttributeLocal-JVM");
    }
    native("js") Integer nativeAttributeLocal3 {
        throw Exception("NativeAttributeLocal-JS");
    }
    
    value x = nativeAttributeLocal1;
    value y = nativeAttributeLocal2;
    nativeAttributeLocal2 = 3;
    value z = nativeAttributeLocal3;
}

native shared Integer nativeAttributeShared1;

native("jvm") shared Integer nativeAttributeShared1 = 1;

native("js") shared Integer nativeAttributeShared1 = 2;


native shared variable Integer nativeAttributeShared2;

native("jvm") shared variable Integer nativeAttributeShared2 = 1;

native("js") shared variable Integer nativeAttributeShared2 = 2;


native shared Integer nativeAttributeShared3;

native("jvm") shared Integer nativeAttributeShared3 {
    throw Exception("NativeAttributeShared-JVM");
}

native("js") shared Integer nativeAttributeShared3 {
    throw Exception("NativeAttributeShared-JS");
}


void testNativeAttributeShared() {
    value x = nativeAttributeShared1;
    value y = nativeAttributeShared2;
    nativeAttributeShared2 = 3;
    value z = nativeAttributeShared3;
}

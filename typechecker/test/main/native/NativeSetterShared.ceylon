native shared Integer nativeSetterShared;
native assign nativeSetterShared;

native("jvm") shared Integer nativeSetterShared => 1;
native("jvm") assign nativeSetterShared { throw Exception("NativeSetterShared-JVM"); }

native("js") shared Integer nativeSetterShared => 1;
native("js") assign nativeSetterShared { throw Exception("NativeSetterShared-JS"); }


void testNativeSetterShared() {
    value x = nativeSetterShared;
    if (x == 1) {
        nativeSetterShared = x;
    }
}

native shared variable Integer nativeVariableSetter;

native("jvm") shared Integer nativeVariableSetter => 1;
native("jvm") assign nativeVariableSetter { throw Exception("NativeVariableSetter-JVM"); }

native("js") shared Integer nativeVariableSetter => 1;
native("js") assign nativeVariableSetter { throw Exception("NativeVariableSetter-JS"); }


void testNativeVariableSetter() {
    value x = nativeVariableSetter;
    if (x == 1) {
        nativeVariableSetter = x;
    }
}

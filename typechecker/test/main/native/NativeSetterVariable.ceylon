native shared Integer nativeSetterVariable;
native assign nativeSetterVariable;

native("jvm") shared variable Integer nativeSetterVariable = 1;

native("js") shared variable Integer nativeSetterVariable = 2;


void testNativeSetterVariable() {
    value x = nativeSetterVariable;
    if (x == 1) {
        nativeSetterVariable = x;
        throw Exception("NativeSetterVariable-JVM");
    } else if (x == 2) {
        nativeSetterVariable = x;
        throw Exception("NativeSetterVariable-JS");
    }
}

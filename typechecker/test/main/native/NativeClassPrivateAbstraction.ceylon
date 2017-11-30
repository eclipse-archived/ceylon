native class NativeClassPrivateAbstraction() {}

native("jvm") class NativeClassPrivateAbstraction() {
    throw Exception("NativeClassPrivateAbstraction-JVM");
}

native("js") class NativeClassPrivateAbstraction() {
    throw Exception("NativeClassPrivateAbstraction-JS");
}

void testNativeClassPrivateAbstraction() {
    value x = NativeClassPrivateAbstraction();
}

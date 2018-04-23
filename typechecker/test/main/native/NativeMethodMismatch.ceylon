native void nativeMethodMismatch1();

native("jvm") void nativeMethodMismatch1() {
}

$error native("js") shared void nativeMethodMismatch1() {
}

native void nativeMethodMismatch2();

$error native("jvm") String nativeMethodMismatch2() {
    throw Exception("NativeMethodMismatch-JVM");
}

$error native("js") Integer nativeMethodMismatch2() {
    throw Exception("NativeMethodMismatch-JS");
}

native void nativeMethodMismatch3(Integer i);

$error native("jvm") void nativeMethodMismatch3(Integer i, Boolean b) {
    throw Exception("NativeMethodMismatch-JVM");
}

native("js") void nativeMethodMismatch3($error String s) {
    throw Exception("NativeMethodMismatch-JS");
}

native shared void nativeMethodMismatch4jvm();

native("jvm") shared void nativeMethodMismatch4jvm() {
    nativeMethodMismatch4js();
}

native shared void nativeMethodMismatch4js();

native("js") shared void nativeMethodMismatch4js() {
    nativeMethodMismatch4jvm();
}

native("jvm")
void testjvm() {
    nativeMethodMismatch4jvm();
}

native("js")
void testjs() {
    nativeMethodMismatch4js();
}

native shared void nativeMethodMismatch5<A,B>();

$error native("jvm") shared void nativeMethodMismatch5<A,B,C>() {
}

$error native("js") shared void nativeMethodMismatch5() {
}

native shared void nativeMethodMismatch6<T>() given T satisfies Usable;

$error native("jvm") shared void nativeMethodMismatch6<T>() {
}

$error native("js") shared void nativeMethodMismatch6<T>() given T satisfies Category<> {
}

native("jvm") class NativeWrongOrder() {
}

native("js") class NativeWrongOrder() {
}

$error native class NativeWrongOrder() {
}

class NativeWrongOrder2() {
    native("jvm") void test() {}
    native("js") void test() {}
    $error native void test();
}

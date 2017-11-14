native shared class NativeErrorHandling() {
    shared native void test() {
        $error foo.bar();
    }
}

native("jvm") shared class NativeErrorHandling() {
}

native("js") shared class NativeErrorHandling() {
}

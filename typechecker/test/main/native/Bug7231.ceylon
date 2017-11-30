native object xxx {
    shared native void f();
}

$error native("jvm") object xxx {}

native class XXX() {
    shared native void f();
}

$error native("jvm") class XXX() {}
native shared Integer nativeMissingMethod();
native shared class NativeMissingClass() {
    native shared void foo();
}

native Integer nativeMissingMethod2();
native("js") Integer nativeMissingMethod2() => 1;

shared void testNativeMissingMethod() {
    nativeMissingMethod();
    nativeMissingMethod2();
    NativeMissingClass nmc = NativeMissingClass();
}

shared void testNativeMissingMethod2(NativeMissingClass nmc) {
}

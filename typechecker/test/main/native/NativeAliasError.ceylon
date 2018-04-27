$error shared native interface NativeAliasError1;
shared native ("jvm") interface NativeAliasError1 {}
shared native ("js") interface NativeAliasError1 {}

shared native interface NativeAliasError2 {}
$error shared native ("jvm") interface NativeAliasError2;
shared native ("js") interface NativeAliasError2 {}

native alias Number => Integer;
native("jvm") alias Number=>Float;
native("js") alias Number=>Integer;

native class My(Number number) {
    shared native Number get();
}
native("jvm") class My($error Number number) {
    $error shared native("jvm") Number get() => number;
}
native("js") class My(Number number) {
    shared native("js") Number get() => number;
}

native class Your() {
    shared native Float get();
}
native("jvm") class Your() {
    Number number = 1.0;
    Float f = number;
    shared native("jvm") Float get() => number;
}
native("js") class Your() {
    Number number = 1;
    Integer i = number;
    shared native("js") Float get() => number.float;
}
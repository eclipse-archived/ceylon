interface NonSharedMethods {
    void f() {
        print("hello");
    }
    shared void x() {
        f();
    }
}
class C() satisfies NonSharedMethods {}
shared void nonSharedMethods() {
    C().x();
}
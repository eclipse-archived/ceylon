@nomodel
class C() {
    shared class D() {
    }
    @nomodel
    void f(C.D foo()) {
    }
    @nomodel
    void m() {
        f(C.D);
    }
}
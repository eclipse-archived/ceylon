@nomodel
class C(void f()) {
    void m(C c) {
        c.f();
        this.f();
    }
}
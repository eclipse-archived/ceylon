@nomodel
void f(void foo()) {
}
@nomodel
class C(Callable<Void> bar) {
    void m(C c) {
        f(c.bar);
        f(this.bar);
    }
}

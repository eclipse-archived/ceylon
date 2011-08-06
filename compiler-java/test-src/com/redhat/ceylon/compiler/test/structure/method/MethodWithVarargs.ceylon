@nomodel
class MethodWithVarargs() {
    shared void f1(Natural... ns) {
        for (Natural n in ns) { }
    }
    shared void f2(Natural i, Natural... ns) {
        for (Natural n in ns) { }
    }
    void m() {
        f1(1, 2, 3);
        f2(1, 2, 3);
        f1();
        f2(1);
    }
}
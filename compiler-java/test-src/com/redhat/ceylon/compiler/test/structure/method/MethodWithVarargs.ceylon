@nomodel
class MethodWithVarargs() {
    shared void f1(Natural... ns) {
        for (Natural n in ns) { }
    }
    shared void f2(Natural i, Natural... ns) {
        for (Natural n in ns) { }
    }
    shared void f3(Object... os) {
        for (Object o in os) { }
    }
    void m() {
        f1(1, 2, 3);
        f2(1, 2, 3);
        f1();
        f2(1);
        f1({1, 2, 3}...);
        f2(1, {2, 3}...);
        f3(1, 2, 3);
        f3({1, 2, 3});
        f3({1, 2, 3}...);
        f3(1, "foo", 5.0);
    }
}
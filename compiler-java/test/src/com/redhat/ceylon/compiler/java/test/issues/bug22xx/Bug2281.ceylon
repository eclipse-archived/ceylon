@noanno
shared void bug2281() {
    interface Bar {
        shared class C() {}
    }
    class X() satisfies Bar {}
    value x = X();
    value c = x.C();
}
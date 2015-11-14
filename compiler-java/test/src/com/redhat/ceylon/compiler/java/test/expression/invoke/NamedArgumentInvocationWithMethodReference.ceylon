class NamedArgumentInvocationWithMethodReference() {
    void m() {
    }
    void m2(Anything() f) {
    }
    void test() {
        m2{
            f=m;
        };
    }
}

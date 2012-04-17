@nomodel
class C<T>() {
    void m(Integer i = 1) {
    }

    shared Integer m2(Integer i = 1) {
        return i;
    }

    Integer m3(Integer i = 1, Integer n = 2*i) {
        return i;
    }

    Integer m4(Integer i, Integer n = 2*i) {
        return i;
    }

    Integer m5<U>(Integer i, T? t = null, U? u = null) {
        return i;
    }
    
    void callsite() {
        m();
        m(2);
        m2();
        m2(2);
        m3();
        m3(5);
        m3(4, 5);
        m4(5);
        m4(4, 5);
        m5<String>(5);
        m5<String>(5, null);
        m5<String>(5, null, "");
    }
}
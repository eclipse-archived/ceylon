@nomodel
void f1(Integer i = 1) {}

@nomodel
Integer f2(Integer i = 1) { return i; }

@nomodel
Integer f3(Integer i = 1, Integer n = 2*i) { return i; }

@nomodel
Integer f4(Integer i, Integer n = 2*i) { return i; }

@nomodel
Integer f5<U>(Integer i, U? u = null) {
    return i;
}

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
        //m5<String>(5);
        //m5<String>(5, null);
        //m5<String>(5, null, "");
    }

    void callsite2() {
        //m{};
        //m{i=2;};
        //m2{};
        //m2(2);
        m3{};
        m3{i=5;};
        m3{i=4; n=5;};
        m3{n=5; i=4;};
        m3{n=5;};
        //m4(5);
        //m4(4, 5);
        //m5<String>(5);
        //m5<String>(5, null);
        //m5<String>(5, null, "");*/
    }    
    
}
class Conditions(String... elems) {

    void x(Anything o) {
        if (is Object o) {
            value n = 1;
            if (o.string.size==n) {}
        }
    }
    
    void y(Object that) {
        if (is Conditions that) {
            value s = elems.count((String e) => true);
            if (1==s) {}
        }
    }
    
    void z(Anything o) {
        if (is Object o) {
            class Inner() {
                Object x = o;
                assert (is String x);
                shared String s = x;
            }
            print(Inner().s);
            
        }
        assert (is Object o);
        class Inner() {
            Object x = o;
            assert (is String x);
            shared String s = x;
        }
        print(Inner().s);
    }

}
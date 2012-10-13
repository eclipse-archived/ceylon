class Conditions(String... elems) {

    void x(Void o) {
        if (is Object o) {
            value n = 1;
            if (o.string.size==n) {}
        }
    }
    
    void y(Object that) {
        if (is Conditions that) {
            value s = elems.count((String e) true);
            if (1==s) {}
        }
    }

}
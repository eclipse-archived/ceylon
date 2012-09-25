import check {...}

shared void test() {
    Boolean a = true;
    Boolean b = false;
    if (a && b) {
        fail("WTF?");
    } else if (a || b) {
        check(a||b, "boolean 1");
    }
    String? c = "X";
    String? d = null;
    if (exists c && exists d) {
        fail("WTF exists");
    } else if (exists c1=c, exists d) {
        check(c1 == "X", "exists");
    }
    String|Integer e = 1;
    if (is Integer e && exists d) {
        fail("WTF is");
    } else if (is Integer e, exists d) {
        check(e>0, "is");
    }
    Integer[] f = {1,2,3};
    Integer[] g = {};
    if (nonempty f && nonempty g) {
        fail("WTF nonempty");
    } else if (nonempty f, nonempty g) {
        check(f.first>0, "nonempty");
    }
    results();
}

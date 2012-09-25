import check {...}

shared void test() {
    Boolean a = true;
    Boolean b = false;
    variable value count := 0;
    if (a && b) {
        fail("WTF?");
    } else if (a || b) {
        check(a||b, "boolean 1");
        count++;
    }
    String? c = "X";
    String? d = null;
    if (exists c && exists d) {
        fail("WTF exists");
    } else if (exists c1=c, exists c1[0]) {
        check(c1 == "X", "exists");
        count++;
    }
    String|Integer e = 1;
    if (is Integer e && exists d) {
        fail("WTF is");
    } else if (is Integer e, exists c) {
        check(e>0, "is");
        count++;
    }
    String[] f = {"a","b","c"};
    Integer[] g = {};
    if (nonempty f && nonempty g) {
        fail("WTF nonempty");
    } else if (nonempty f, nonempty f.first) {
        check(f.first.uppercased=="A", "nonempty");
        count++;
    }
    check(count==4, "some conditions were not met: " count " instead of 4");
    results();
}

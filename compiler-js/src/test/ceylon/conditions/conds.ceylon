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
    Void zz = 1;
    assert(is Integer zz2=zz, zz2 > 0);
    check(zz2==1, "special -> boolean");
    //and now some comprehensions
    Sequence<Integer?> seq1 = { 1, 2, 3, null, 4, 5, null, 6, 7, null, 10};
    check({ for (i in seq1) if (exists i, i%2==0) i*10 }=={20,40,60,100},"comprehension [1]");
    // !is
    if (!is String zz) {
        check(true, "!is");
    } else {
        fail("!is");
    }
    if (!is String zz3=zz) {
        check(is Integer zz3, "!is with variable [1]");
        check(!is String zz3, "!is with variable [2]");
    } else {
        fail("!is (again)");
    }
    assert(!is String zz4=zz, is Integer zz4);
    results();
}

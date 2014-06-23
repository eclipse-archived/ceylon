import check { ... }

void test_if() {
    //True, with else
    if (true) {
        check(true,"if(true)");
    } else {
        fail("Never happen");
    }
    //False, with else
    if (false) {
        fail("Never happen");
    } else {
        check(true,"if(false)");
    }
    //without else
    if (2+2 == 4) {
        check(true,"if without else");
    }
    //chained if's
    if (1+2 == 4) {
        fail("can't happen");
    } else if (2+2 == 5) {
        fail("No way");
    } else {
        check(true,"Chained if's with else");
    }
    //chained if's without else
    if (1+2 == 4) {
        fail("can't happen");
    } else if (2+2 == 4) {
        check(true,"Chained if's without else");
    }
    //More complex conditions
    if (2>1 && 1<2) {
        check(true,"if &&");
    } else { fail("if &&"); }
    if ((1>2 || 1>0) && 1<2) {
        check(true,"if (||)&&");
    } else { fail("if (||)&&"); }
}

void test_while() {
    variable Integer i = 0;
    while (i < 2) {
        i = i+1;
    }
    check(i==2, "while");
    while (i >= 2 && i < 4) {
        i = i+1;
    }
    check(i==4,"while");
}

void testIfExists() {
    String? s1 = null;
    String? s2 = "";
    if (exists s1) {
        fail("if (exists x)");
    }
    if (exists s2) {
    } else {
        fail("if (exists x)");
    }
    if (exists s3 = s1) {
        fail("if (exists x=y)");
    }
    String? s4 = "hi";
    if (exists s3 = s4) {
        check(s3=="hi", "if (exists x=y)");
    } else {
        fail("if (exists x=y)");
    }
    if (exists s3 = s2) {
        if (exists s5 = s4) {
            check(s3=="", "if (exists x=y) nested");
            check(s5=="hi", "if (exists x=y) nested");
        } else {
            fail("if (exists x=y) nested");
        }
    } else {
        fail("if (exists x=y) nested");
    }
    if (exists len = s4?.size) {
        check(len==2, "if (exists x=expr)");
    } else {
        fail("if (exists x=expr)");
    }
    variable Integer cnt = 0;
    String? s5 { ++cnt; return "ok"; }
    if (exists x = s5) {
        check(x=="ok", "if (exists x=y) with getter [value: ``x``]");
    } else {
        fail("if (exists x=y) with getter [exists]");
    }
    check(cnt==1, "if (exists x=y) with getter [calls: ``cnt``]");
}

void testWhileExists() {
    String? s1 = null;
    String? s2 = "";
    variable Integer i1 = 0;
    while (exists s1) {
        ++i1;
        break;
    }
    check(i1==0, "while (exists x)");
    i1 = 0;
    while (exists s2) {
        if (++i1 >= 2) {
            break;
        }
    }
    check(i1==2, "while (exists x)");
    i1 = 0;
    while (exists s3 = s1) {
        ++i1;
        break;
    }
    check(i1==0, "while (exists x=y)");
    variable String? s4 = "hi";
    i1 = 0;
    while (exists s3 = s4) {
        ++i1;
        s4 = null;
    }
    check(i1==1, "while (exists x=y)");
    i1 = 0;
    while (exists s3 = s2) {
        s4 = "hi";
        variable Integer i2 = 0;
        while (exists s5 = s4) {
            if (++i2 == 2) {
                s4 = null;
            }
            ++i1;
        }
        if (i1 >= 4) {
            break;
        }
    } 
    check(i1==4, "while (exists x=y) nested");
    s4 = "hi";
    i1 = 0;
    while (exists len = s4?.size) {
        check(len==2, "while (exists x=expr)");
        s4 = null;
        ++i1;
    }
    check(i1==1, "while (exists x=expr)");
}

void testIfNonempty() {
    String[] s1 = [];
    String[] s2 = [ "abc" ];
    if (nonempty s1) {
        fail("if (nonempty x)");
    }
    if (nonempty s2) {
    } else {
        fail("if (nonempty x)");
    }
    if (nonempty s3 = s1) {
        fail("if (nonempty x=y)");
    }
    String[] s4 = [ "hi" ];
    if (nonempty s3 = s4) {
        check(s3.first=="hi", "if (nonempty x=y)");
    } else {
        fail("if (nonempty x=y)");
    }
    if (nonempty s3 = s2) {
        if (nonempty s5 = s4) {
            check(s3.first=="abc", "if (nonempty x=y) nested");
            check(s5.first=="hi", "if (nonempty x=y) nested");
        } else {
            fail("if (nonempty x=y) nested");
        }
    } else {
        fail("if (nonempty x=y) nested");
    }
    if (nonempty s6 = s4.segment(0, 1)) {
        check(s6.first=="hi", "if (nonempty x=expr)");
    } else {
        fail("if (nonempty x=expr)");
    }
}

shared void test() {
    test_if();
    test_while();
    testIfExists();
    testWhileExists();
    testIfNonempty();
    value assertsBefore=assertionCount();
    testBlocks();
    check(assertionCount()==assertsBefore+6, "block assertions skipped: ``(assertsBefore+6-assertionCount())``");
    testConditions();
    testSwitches();
    results();
}

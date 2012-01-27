variable Integer assertionCount:=0;
variable Integer failureCount:=0;

shared void assert(Boolean assertion, String message="") {
    assertionCount+=1;
    if (!assertion) {
        failureCount+=1;
        print("assertion failed \"" message "\"");
    }
}

shared void fail(String message) {
    assert(false, message);
}

shared void results() {
    print("assertions " assertionCount 
          ", failures " failureCount "");
}

void test_if() {
    //True, with else
    if (true) {
        print("if(true) OK");
    } else {
        fail("Never happen");
    }
    //False, with else
    if (false) {
        fail("Never happen");
    } else {
        print("if(false) OK");
    }
    //without else
    if (2+2 == 4) {
        print("if without else OK");
    }
    //chained if's
    if (1+2 == 4) {
        fail("can't happen");
    } else if (2+2 == 5) {
        fail("No way");
    } else {
        print("Chained if's with else OK");
    }
    //chained if's without else
    if (1+2 == 4) {
        fail("can't happen");
    } else if (2+2 == 4) {
        print("Chained if's without else OK");
    }
    //More complex conditions
    if (2>1 && 1<2) {
        print("if && OK");
    } else { fail("if &&"); }
    if ((1>2 || 1>0) && 1<2) {
        print("if (||)&& OK");
    } else { fail("if (||)&&"); }
}

void test_while() {
    variable Integer i := 0;
    while (i < 2) {
        i := i+1;
    }
    assert(i==2, "while");
    while (i >= 2 && i < 4) {
        i := i+1;
    }
    assert(i==4,"while");
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
        assert(s3=="hi", "if (exists x=y)");
    } else {
        fail("if (exists x=y)");
    }
    if (exists s3 = s2) {
        if (exists s5 = s4) {
            assert(s3=="", "if (exists x=y) nested");
            assert(s5=="hi", "if (exists x=y) nested");
        } else {
            fail("if (exists x=y) nested");
        }
    } else {
        fail("if (exists x=y) nested");
    }
    if (exists len = s4?.size) {
        assert(len==2, "if (exists x=expr)");
    } else {
        fail("if (exists x=expr)");
    }
}

void testWhileExists() {
    String? s1 = null;
    String? s2 = "";
    variable Integer i1 := 0;
    while (exists s1) {
        ++i1;
        break;
    }
    assert(i1==0, "while (exists x)");
    i1 := 0;
    while (exists s2) {
        if (++i1 >= 2) {
            break;
        }
    }
    assert(i1==2, "while (exists x)");
    i1 := 0;
    while (exists s3 = s1) {
        ++i1;
        break;
    }
    assert(i1==0, "while (exists x=y)");
    variable String? s4 := "hi";
    i1 := 0;
    while (exists s3 = s4) {
        ++i1;
        s4 := null;
    }
    assert(i1==1, "while (exists x=y)");
    i1 := 0;
    while (exists s3 = s2) {
        s4 := "hi";
        variable Integer i2 := 0;
        while (exists s5 = s4) {
            if (++i2 == 2) {
                s4 := null;
            }
            ++i1;
        }
        if (i1 >= 4) {
            break;
        }
    } 
    assert(i1==4, "while (exists x=y) nested");
    s4 := "hi";
    i1 := 0;
    while (exists len = s4?.size) {
        assert(len==2, "while (exists x=expr)");
        s4 := null;
        ++i1;
    }
    assert(i1==1, "while (exists x=expr)");
}

class MySequence<out Element>(Sequence<Element> seq)
            satisfies Sequence<Element> {
    shared actual Integer lastIndex { return seq.lastIndex; }
    shared actual Element first { return seq.first; }
    shared actual Element[] rest { return seq.rest; }
    shared actual Element? item(Integer index) { return seq[index]; }
    shared actual Element[] span(Integer from, Integer? to) { return seq.span(from, to); }
    shared actual Element[] segment(Integer from, Integer length) { return seq.segment(from, length); }
    shared actual MySequence<Element> clone { return this; }
    shared actual String string { return seq.string; }
}

void testIfNonempty() {
    String[] s1 = {};
    String[] s2 = { "abc" };
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
    String[] s4 = { "hi" };
    if (nonempty s3 = s4) {
        assert(s3.first=="hi", "if (nonempty x=y)");
    } else {
        fail("if (nonempty x=y)");
    }
    if (nonempty s3 = s2) {
        if (nonempty s5 = s4) {
            assert(s3.first=="abc", "if (nonempty x=y) nested");
            assert(s5.first=="hi", "if (nonempty x=y) nested");
        } else {
            fail("if (nonempty x=y) nested");
        }
    } else {
        fail("if (nonempty x=y) nested");
    }
    if (nonempty s6 = s4.segment(0, 1)) {
        assert(s6.first=="hi", "if (nonempty x=expr)");
    } else {
        fail("if (nonempty x=expr)");
    }
    String[] s = MySequence<String>({"hi"});
    if (nonempty s) {
    } else {
        fail("if (nonempty x) custom sequence");
    }
}

shared void test() {
    test_if();
    test_while();
    testIfExists();
    testWhileExists();
    testIfNonempty();
    results();
}

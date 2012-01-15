void expect(Equality actual, Equality expected, String text) {
    if (actual == expected) {
        print("[ok] " + text + ": '" + actual.string + "'");
    } else {
        print("[NOT OK] " + text + ": actual='" + actual.string + "', expected='"
              + expected.string + "'");
    }
}
void succeed(String text) {
    print("[ok] " + text);
}
void fail(String text) {
    print("[NOT OK] " + text);
}

void test_if() {
    //True, with else
    if (true) {
        print("if(true) OK");
    } else {
        print("Never happen");
    }
    //False, with else
    if (false) {
        print("Never happen");
    } else {
        print("if(false) OK");
    }
    //without else
    if (2+2 == 4) {
        print("if without else OK");
    }
    //chained if's
    if (1+2 == 4) {
        print("can't happen");
    } else if (2+2 == 5) {
        print("No way");
    } else {
        print("Chained if's with else OK");
    }
    //chained if's without else
    if (1+2 == 4) {
        print("can't happen");
    } else if (2+2 == 4) {
        print("Chained if's without else OK");
    }
    //More complex conditions
    if (2>1 && 1<2) {
        print("if && OK");
    } else { print("if && FAIL"); }
    if ((1>2 || 1>0) && 1<2) {
        print("if (||)&& OK");
    } else { print("if (||)&& FAIL"); }
}

void test_while() {
    variable Integer i := 0;
    while (i < 2) {
        print("while OK");
        i := i+1;
    }
    while (i >= 2 && i < 4) {
        print("while OK");
        i := i+1;
    }
}

void testIfExists() {
    String? s1 = null;
    String? s2 = "";
    if (exists s1) {
        fail("if (exists x)");
    } else {
        succeed("if (exists x)");
    }
    if (exists s2) {
        succeed("if (exists x)");
    } else {
        fail("if (exists x)");
    }
    if (exists s3 = s1) {
        fail("if (exists x=y)");
    } else {
        succeed("if (exists x=y)");
    }
    String? s4 = "hi";
    if (exists s3 = s4) {
        expect(s3, "hi", "if (exists x=y)");
    } else {
        fail("if (exists x=y)");
    }
    if (exists s3 = s2) {
        if (exists s5 = s4) {
            expect(s3, "", "if (exists x=y) nested");
            expect(s5, "hi", "if (exists x=y) nested");
        } else {
            fail("if (exists x=y) nested");
        }
    } else {
        fail("if (exists x=y) nested");
    }
    if (exists len = s4?.size) {
        expect(len, 2, "if (exists x=expr)");
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
    expect(i1, 0, "while (exists x)");
    i1 := 0;
    while (exists s2) {
        if (++i1 >= 2) {
            break;
        }
    }
    expect(i1, 2, "while (exists x)");
    i1 := 0;
    while (exists s3 = s1) {
        ++i1;
        break;
    }
    expect(i1, 0, "while (exists x=y)");
    variable String? s4 := "hi";
    i1 := 0;
    while (exists s3 = s4) {
        ++i1;
        s4 := null;
    }
    expect(i1, 1, "while (exists x=y)");
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
    expect(i1, 4, "while (exists x=y) nested");
    s4 := "hi";
    i1 := 0;
    while (exists len = s4?.size) {
        expect(len, 2, "while (exists x=expr)");
        s4 := null;
        ++i1;
    }
    expect(i1, 1, "while (exists x=expr)");
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
    } else {
        succeed("if (nonempty x)");
    }
    if (nonempty s2) {
        succeed("if (nonempty x)");
    } else {
        fail("if (nonempty x)");
    }
    if (nonempty s3 = s1) {
        fail("if (nonempty x=y)");
    } else {
        succeed("if (nonempty x=y)");
    }
    String[] s4 = { "hi" };
    if (nonempty s3 = s4) {
        expect(s3.first, "hi", "if (nonempty x=y)");
    } else {
        fail("if (nonempty x=y)");
    }
    if (nonempty s3 = s2) {
        if (nonempty s5 = s4) {
            expect(s3.first, "abc", "if (nonempty x=y) nested");
            expect(s5.first, "hi", "if (nonempty x=y) nested");
        } else {
            fail("if (nonempty x=y) nested");
        }
    } else {
        fail("if (nonempty x=y) nested");
    }
    if (nonempty s6 = s4.segment(0, 1)) {
        expect(s6.first, "hi", "if (nonempty x=expr)");
    } else {
        fail("if (nonempty x=expr)");
    }
    String[] s = MySequence<String>({"hi"});
    if (nonempty s) {
        succeed("if (nonempty x) custom sequence");
    } else {
        fail("if (nonempty x) custom sequence");
    }
}

shared void test() {
    print("--- Start flow control tests ---");
    test_if();
    test_while();
    testIfExists();
    testWhileExists();
    testIfNonempty();
    print("--- End flow control tests ---");
}

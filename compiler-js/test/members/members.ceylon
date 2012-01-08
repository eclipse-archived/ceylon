void expect(Equality actual, Equality expected, String text) {
    if (actual == expected) {
        print("[ok] " + text + ": '" + actual.string + "'");
    } else {
        print("[NOT OK] " + text + ": actual='" + actual.string + "', expected='"
              + expected.string + "'");
    }
}

shared class Counter(Integer initialCount=0) {
    variable value currentCount:=initialCount;
    shared Integer count {
        return currentCount;
    }
    shared void inc() {
        currentCount:=currentCount+1; 
    }
    shared Integer initialCount {
        return initialCount;
    }
    shared actual String string {
        return "Counter[" + count.string + "]";
    }
}

class Issue10C1(Integer arg1) {
    Integer i1 = 3;
    Integer i2 = 5;
    shared default Integer i3 = 7;
    shared Integer f1() { return arg1; }
    shared Integer f2() { return i1; }
    shared Integer f3() { return i2; } 
    shared Integer f4() { return i3; }
    Integer f5() { return 9; }
    shared Integer f6() { return f5(); }
    Integer f7() { return 11; }
    shared Integer f8() { return f7(); }
    shared default Integer f9() { return 13; }
    shared Integer f10() { return f9(); }
}
class Issue10C2(Integer arg1) extends Issue10C1(1) {
    Integer i1 = 4;
    shared Integer i2 = 6;
    shared actual Integer i3 = 8;
    shared Integer f11() { return arg1; }
    shared Integer f12() { return i1; }
    Integer f5() { return 10; }
    shared Integer f13() { return f5(); }
    shared Integer f7() { return 12; }
    shared actual Integer f9() { return 14; }
}

void testIssue10() {
    value obj = Issue10C2(2);
    expect(obj.f1(), 1, "Issue #10 (parameter)");
    expect(obj.f11(), 2, "Issue #10 (parameter)");
    expect(obj.f2(), 3, "Issue #10 (non-shared attribute)");
    expect(obj.f12(), 4, "Issue #10 (non-shared attribute)");
    expect(obj.f3(), 5, "Issue #10 (non-shared attribute)");
    expect(obj.i2, 6, "Issue #10 (shared attribute)");
    expect(obj.f4(), 8, "Issue #10 (shared attribute)");
    expect(obj.i3, 8, "Issue #10 (shared attribute)");
    expect(obj.f6(), 9, "Issue #10 (non-shared method)");
    expect(obj.f13(), 10, "Issue #10 (non-shared method)");
    expect(obj.f8(), 11, "Issue #10 (non-shared method)");
    expect(obj.f7(), 12, "Issue #10 (shared method)");
    expect(obj.f10(), 14, "Issue #10 (shared method)");
    expect(obj.f9(), 14, "Issue #10 (shared method)");
}

shared void test() {
    value c = Counter(0);
    print(c.count);
    c.inc(); c.inc();
    print(c.count);
    print(c);
    
    testIssue10();
}
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
}
class Issue10C2(Integer arg1) extends Issue10C1(1) {
    Integer i1 = 4;
    shared Integer i2 = 6;
    shared actual Integer i3 = 8;
    shared Integer f5() { return arg1; }
    shared Integer f6() { return i1; }
}

void testIssue10() {
    value obj = Issue10C2(2);
    expect(obj.f1(), 1, "Issue #10 (parameter)");
    expect(obj.f5(), 2, "Issue #10 (parameter)");
    expect(obj.f2(), 3, "Issue #10 (non-shared attribute)");
    expect(obj.f6(), 4, "Issue #10 (non-shared attribute)");
    expect(obj.f3(), 5, "Issue #10 (non-shared attribute)");
    expect(obj.i2, 6, "Issue #10 (shared attribute)");
    expect(obj.f4(), 8, "Issue #10 (shared attribute)");
    expect(obj.i3, 8, "Issue #10 (shared attribute)");
}

shared void test() {
    value c = Counter(0);
    print(c.count);
    c.inc(); c.inc();
    print(c.count);
    print(c);
    
    testIssue10();
}
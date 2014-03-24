variable Integer assertions=0;
variable Integer failures=0;

shared void initAssert() {
  assertions=0;
  failures=0;
}

shared void check(Boolean assertion, String message="") {
    assertions++;
    if (!assertion) {
        failures++;
        print("assertion failed \"``message``\"");
    }
}

shared void checkEqual(Object actual, Object expected, String message="") {
    assertions++;
    if (actual != expected) {
        failures++;
        print("assertion failed \"``message``\": '``actual``'!='``expected``'");
    }
}

shared void fail(String message) {
    check(false, message);
}

shared void results() {
    print("assertions ``assertions``, failures ``failures``");
}

shared Integer assertionCount() { return assertions; }

@test
shared void test() {
    //This is as good a place as any to test new 'assert' keyword
    String? ms = "x";
    assert(exists ds = ms);
    check(ds.uppercased=="X", "assert exists");
    assert(2+2==4);
    Integer[] mseq = [ 1, 2, 3 ];
    assert(nonempty dseq = mseq);
    check(dseq.first == 1, "assert nonempty [1]");
    String|Integer mt = 5;
    assert(is Integer dt=mt);
    check(dt+1 == 6, "assert is [1]");
    assert(is Integer mt);
    assert(nonempty mseq, mseq[1] exists);
    assert(exists ms2=ms, exists m2_1=ms);
    assert(is String ms3=ms, ms3[0] exists);
    try {
        assert(2+2==5);
        fail("check assert [1]");
    } catch (AssertionError ex) {
        check("Assertion failed: '2+2==5'" in ex.message, "Assertion message");
    }
    try {
        "ms is a looong String"
        assert(is String ms, ms[1100] exists);
        fail("check assert [2]");
    } catch (AssertionError ex) {
        check("ms is a looong String: 'is String ms, ms[1100] exists'" in ex.message, "custom assert message was ``ex.message``");
    }
    try {
        value length = "hello world".size;
        "length must be less than 10"
        assert (length < 10);
        fail("check assert [3]");
    } catch (AssertionError ex) {
        check("length must be less than 10: 'length < 10'" in ex.message, "custom assert message with implicit doc ``ex.message``");
    }
    check(ms2==ms3, "assertion values");
    results();
}

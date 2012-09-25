variable Integer assertions:=0;
variable Integer failures:=0;

shared void initAssert() {
  assertions:=0;
  failures:=0;
}

shared void check(Boolean assertion, String message="") {
    assertions++;
    if (!assertion) {
        failures++;
        print("assertion failed \"" message "\"");
    }
}

shared void checkEqual(Object actual, Object expected, String message="") {
    assertions++;
    if (actual != expected) {
        failures++;
        print("assertion failed \"" message "\": '" actual "'!='" expected "'");
    }
}

shared void fail(String message) {
    check(false, message);
}

shared void results() {
    print("assertions " assertions 
          ", failures " failures "");
}

shared Integer assertionCount() { return assertions; }

shared void test() {
    //This is as good a place as any to test new 'assert' keyword
    String? ms = "x";
    assert(exists ds = ms);
    check(ds.uppercased=="X", "assert exists");
    assert(2+2==4);
    Integer[] mseq = { 1, 2, 3 };
    assert(nonempty dseq = mseq);
    check(dseq.first == 1, "assert nonempty [1]");
    String|Integer mt = 5;
    assert(is Integer dt=mt);
    check(dt+1 == 6, "assert is [1]");
    assert(is Integer mt);
    assert(nonempty mseq);
    assert(exists ms);
    results();
}

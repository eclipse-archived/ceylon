variable Integer assertions:=0;
variable Integer failures:=0;

shared void initAssert() {
  assertions:=0;
  failures:=0;
}

shared void assert(Boolean assertion, String message="") {
    assertions++;
    if (!assertion) {
        failures++;
        print("assertion failed \"" message "\"");
    }
}

shared void assertEqual(Object actual, Object expected, String message="") {
    assertions++;
    if (actual != expected) {
        failures++;
        print("assertion failed \"" message "\": '" actual "'!='" expected "'");
    }
}

shared void fail(String message) {
    assert(false, message);
}

shared void results() {
    print("assertions " assertions 
          ", failures " failures "");
}

shared Integer assertionCount() { return assertions; }

shared void test() {}

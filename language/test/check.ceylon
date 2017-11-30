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
        print("**** ASSERTION FAILED \"``message``\" ****");
    }
}

shared void checkEqual(Object actual, Object expected, String message="") {
    assertions++;
    if (actual != expected) {
        failures++;
        print("**** ASSERTION FAILED \"``message``\": '``actual``'!='``expected``' ****");
    }
}

shared void fail(String message) {
    check(false, message);
}

shared void results() {
    print("assertions ``assertions``, failures ``failures``");
}

shared void resultsAndAssert() {
    results();
    if (failures!=0) {
        throw Exception("There were ``failures`` failures (out of ``assertions`` assertions)");
    }
}

shared Integer assertionCount() => assertions;

shared void resultsAndExit() {
    results();
    process.exit(failures == 0 then 0 else 1);
}


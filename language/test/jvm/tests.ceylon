import check {
    check,
    fail,
    initAssert,
    results,
    resultsAndAssert,
    resultsAndExit
}

void runTests() {
    print("** Running JVM Tests **");
    bug365();
    bug200();
    bug6656();
    print("JVM Arrays");
    testArrays();
}

shared void run() {
    initAssert();
    runTests();
    results();
}

shared void runAndAssert() {
    initAssert();
    runTests();
    resultsAndAssert();
}

shared void runAndExit() {
    initAssert();
    runTests();
    resultsAndExit();
}

shared void test() {
    run();
}

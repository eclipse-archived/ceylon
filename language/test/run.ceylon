shared void run() {
    print("Testing equality");
    equality();
    //complex();
    print("Testing booleans");
    booleans();
    print("Testing numbers");
    numbers();
    print("Testing lists");
    lists();
    print("Testing sequences");
    sequences();
    print("Testing characters");
    characters();
    print("Testing strings");
    strings();
    print("Testing iterators");
    iterators();
    print("Testing entries and ranges");
    entriesAndRanges();
    print("Testing comparables");
    comparables();
    print("Testing clones");
    clones();
    print("Testing types");
    types();
    print("Testing exceptions");
    exceptions();
    print("Testing operators");
    operators();
    print("Testing callables");
    callables();
    print("Misc tests");
    misc();
    print("Array/Collection/FixedSized tests");
    testArrays();
    print("Map tests");
    testMaps();
    print("Set tests");
    testSets();
    print("Iterables test: map/fold/filter/find/sort etc");
    testIterables();
    print("Testing comprehensions and comprehension-related functions");
    comprehensions();
    print("Testing process");
    testProcess();
    print("Interfaces");
    testSatisfaction();
    print("Test sort");
    testSort();
    results();
}

shared void runAndAssert() {
    run();
    if (failureCount!=0) {
        throw Exception("There were " failureCount " failures (out of " assertionCount " assertions)");
    }
}

shared void runAndExit() {
    run();
    if (failureCount!=0) {
        print("There were " failureCount " failures (out of " assertionCount " assertions)");
    }
    process.exit(failureCount ==0 then 0 else 1);   
}

shared void test() { run(); }

shared void run() {
    print("Equality");
    equality();
    //complex();
    print("Booleans");
    booleans();
    print("Numbers");
    numbers();
    print("Lists & sequences");
    lists();
    sequences();
    print("Characters & strings");
    characters();
    strings();
    print("Iterators");
    iterators();
    print("Entries and ranges");
    entriesAndRanges();
    print("Comparables");
    comparables();
    print("Clones");
    clones();
    print("Types");
    types();
    print("Exceptions");
    exceptions();
    print("Operators");
    operators();
    print("Callables");
    callables();
    print("Miscellaneous");
    misc();
    testSort();
    print("Array/Collection/FixedSized");
    testArrays();
    print("Map & Set tests");
    testMaps();
    testSets();
    print("Iterables: map/fold/filter/find/sort etc");
    testIterables();
    print("Comprehensions and comprehension-related functions");
    comprehensions();
    print("Process");
    testProcess();
    print("Interfaces (satisfaction)");
    testSatisfaction();
    print("Backward spans and segments");
    spansegments();
    results();
}

shared void runAndAssert() {
    run();
    print("There were " failureCount " failures (out of " assertionCount " assertions)");
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

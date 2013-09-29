shared void run() {
    print("Equality");
    equality();
    //complex();
    print("Booleans & Numbers");
    booleans();
    numbers();
    print("Lists & sequences");
    lists();
    sequences();
    print("Range");
    testRange();
    print("Characters & strings");
    characters();
    strings();
    print("Iterators, Entries, Ranges, spans & segments");
    iterators();
    entriesAndRanges();
    spansegments();
    print("Comparables & Clones");
    comparables();
    clones();
    print("Types/satisfying interfaces");
    types();
    testSatisfaction();
    print("Exceptions");
    exceptions();
    print("Operators");
    operators();
    print("Callables");
    callables();
    print("Miscellaneous");
    misc();
    switches();
    testSort();
    print("Array/Collection");
    testArrays();
    print("Map & Set tests");
    testMaps();
    testSets();
    print("Iterables & comprehensions");
    testIterables();
    comprehensions();
    print("Process");
    testProcess();
    print("Runtime");
    testRuntime();
    print("Operating System");
    testOperatingSystem();
    print("System");
    testSystem();
    print("Tuples");
    tuples();
    testCurries();
    
    // ATTENTION!
    // When you add new test methods here make sure they are "shared" and marked "@test"!

    results();
}

shared void runAndAssert() {
    run();
    print("There were ``failureCount`` failures (out of ``assertionCount`` assertions)");
    if (failureCount!=0) {
        throw Exception("There were ``failureCount`` failures (out of ``assertionCount`` assertions)");
    }
}

shared void runAndExit() {
    run();
    if (failureCount!=0) {
        print("There were ``failureCount`` failures (out of ``assertionCount`` assertions)");
    }
    process.exit(failureCount ==0 then 0 else 1);   
}

shared void test() { run(); }

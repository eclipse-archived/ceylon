shared void run() {
    testInlineExpressions();
    print("Equality");
    equality();
    print("Comparable");
    comparables();
    bug1561();
    print("Clones");
    clones();
    //complex();
    print("Booleans");
    booleans();
    print("Numbers");
    numbers();
    print("Bytes");
    bytes();
    print("Characters");
    characters();
    print("Strings");
    strings();
    print("Lists");
    lists();
    print("Sequences");
    sequences();
    print("Tuples");
    tuples();
    print("Arrays");
    testArrays();
    print("Iterables");
    iterators();
    print("Entries & ranges");
    entriesAndRanges();
    print("Spans & measures");
    spanmeasures();
    print("Range");
    testRange();
    testRecursiveRange();
    print("Map");
    testMaps();
    print("Sets");
    testSets();
    print("Iterables");
    testIterables();
    print("Comprehensions");
    comprehensions();
    print("Types & satisfying interfaces");
    types();
    testSatisfaction();
    print("Use-site variance");
    testUseSiteVariance();
    print("Exceptions");
    exceptions();
    print("Operators");
    operators();
    print("Miscellaneous");
    misc();
    print("Switches");
    switches();
    print("Sort");
    testSort();
    print("Constructors");
    testConstructors();
    print("Process, runtime, system, operatingSystem");
    testProcess();
    testRuntime();
    testSystem();
    testOperatingSystem();
    print("Callables & curry");
    callables();
    testCurries();
    print("Resources");
    testResources();
    print("Predicates");
    testPredicates();
    print("Destructuring");
    testDestructuring();
    print("Metamodel in default module");
    testMetamodelInDefaultModule();
    print("Native extensions");
    testNativeClassesAndObjects();

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

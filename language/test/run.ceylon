void runTests() {
    print("** Running Language Module Tests **");
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
    print("Memoized");
    memoized();
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
    print("Statics");
    testStatic();
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
    testNativeInterfaces();
    print("Contextual");
    testContextual();
    
    // ATTENTION!
    // When you add new test methods here make sure they are "shared" and marked "@test"!
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

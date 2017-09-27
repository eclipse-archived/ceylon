import compiled120{test}

shared void runOn120() {
    test("1.2.0");
}
shared void runOnLatest() {
    assert(exists version = process.arguments[0]);
    test(version);
}
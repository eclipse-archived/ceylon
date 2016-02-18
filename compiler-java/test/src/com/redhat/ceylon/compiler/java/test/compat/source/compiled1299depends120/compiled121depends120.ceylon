import compiled120{test}

shared void runOnLatest() {
    assert(exists version = process.arguments[0]);
    test(version);
}
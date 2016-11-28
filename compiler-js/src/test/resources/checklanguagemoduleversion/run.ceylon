shared void run() {
    assert(exists v = process.arguments.first);
    assert(v == language.version);
    print("Expected ``v`` got ``language.version``");
}

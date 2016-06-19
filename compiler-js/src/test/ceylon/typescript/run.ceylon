import tsmodule {
    ...
}

void testEmptyString() {
    String es = emptyString;
    assert (es == "");
    assert (emptyString == "");
}

shared void run() {
    testEmptyString();
}

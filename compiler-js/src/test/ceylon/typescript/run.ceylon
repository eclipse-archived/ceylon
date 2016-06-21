import tsmodule {
    ...
}

void testConstEmptyString() {
    String es = constEmptyString;
    assert (es == "");
    assert (constEmptyString == "");
}

void testVarEmptyString() {
    String es = varEmptyString;
    assert (es == "");
    assert (varEmptyString == "");
    varEmptyString = "a";
    assert (varEmptyString == "a");
    varEmptyString = "";
}

shared void run() {
    testConstEmptyString();
    testVarEmptyString();
}

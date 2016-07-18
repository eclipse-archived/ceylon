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

void testGetVarEmptyString() {
    String es = getVarEmptyString();
    assert (es == "");
    assert (getVarEmptyString() == "");
    varEmptyString = "a";
    assert (getVarEmptyString() == "a");
    varEmptyString = "";
}

void testStringIdentity() {
    assert (stringIdentity("") == "");
    assert (stringIdentity("foo") == "foo");
    assert (stringIdentity { "bar"; } == "bar");
    assert (stringIdentity { s = "baz"; } == "baz");
}

shared void run() {
    testConstEmptyString();
    testVarEmptyString();
    testGetVarEmptyString();
    testStringIdentity();
}

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

void testNumberIdentity() {
    assert (numberIdentity(4.2) == 4.2);
    assert (numberIdentity(1.0) == 1.0);
    assert (numberIdentity(1) == 1.0);
    assert (numberIdentity(1) == 1);
}

shared void run() {
    testConstEmptyString();
    testVarEmptyString();
    testGetVarEmptyString();
    testStringIdentity();
    testNumberIdentity();
}

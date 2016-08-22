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

void testBooleanIdentity() {
    assert (booleanIdentity(true));
    assert (!booleanIdentity(false));
}

suppressWarnings ("unusedDeclaration")
void testVoidFunction() {
    Anything a1 = voidFunction(1);
    Anything a2 = voidFunction("string");
    Anything a3 = voidFunction(testVoidFunction);
}

void testStringBox() {
    value sb = StringBox("s1");
    assert (sb.s == "s1");
    sb.setS("s2");
    assert (sb.getS() == "s2");
    sb.s = "s3";
    assert (sb.getS() == "s3");
    sb.setS("s4");
    assert (sb.s == "s4");
}

shared void run() {
    testConstEmptyString();
    testVarEmptyString();
    testGetVarEmptyString();
    testStringIdentity();
    testNumberIdentity();
    testBooleanIdentity();
    testVoidFunction();
    testStringBox();
}

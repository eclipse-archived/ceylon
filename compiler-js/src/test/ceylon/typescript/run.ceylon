import tsmodule {
    ...
}

void errlog(Object o) {
    dynamic {
        console.error(o);
    }
}

void assertEquals(Object expected, Object actual) {
    if (expected != actual) {
        errlog(expected);
        errlog(actual);
        throw AssertionError("expected != actual");
    }
}

void testConstEmptyString() {
    String es = constEmptyString;
    assertEquals { expected = ""; actual = es; };
    assertEquals { expected = ""; actual = constEmptyString; };
}

void testVarEmptyString() {
    String es = varEmptyString;
    assertEquals { expected = ""; actual = es; };
    assertEquals { expected = ""; actual = varEmptyString; };
    varEmptyString = "a";
    assertEquals { expected = "a"; actual = varEmptyString; };
    varEmptyString = "";
}

void testGetVarEmptyString() {
    String es = getVarEmptyString();
    assertEquals { expected = ""; actual = es; };
    assertEquals { expected = ""; actual = getVarEmptyString(); };
    varEmptyString = "a";
    assertEquals { expected = "a"; actual = getVarEmptyString(); };
    varEmptyString = "";
}

void testStringIdentity() {
    assertEquals { expected = ""; actual = stringIdentity(""); };
    assertEquals { expected = "foo"; actual = stringIdentity("foo"); };
    assertEquals { expected = "bar"; actual = stringIdentity { "bar"; }; };
    assertEquals { expected = "baz"; actual = stringIdentity { s = "baz"; }; };
}

void testNumberIdentity() {
    assertEquals { expected = 4.2; actual = numberIdentity(4.2); };
    assertEquals { expected = 1.0; actual = numberIdentity(1.0); };
    assertEquals { expected = 1; actual = numberIdentity(1.0); };
    assertEquals { expected = 1; actual = numberIdentity(1); };
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
    assertEquals { expected = "s1"; actual = sb.s; };
    sb.setS("s2");
    assertEquals { expected = "s2"; actual = sb.getS(); };
    sb.s = "s3";
    assertEquals { expected = "s3"; actual = sb.getS(); };
    sb.setS("s4");
    assertEquals { expected = "s4"; actual = sb.s; };
    StringBox sb2 = sb.self();
    assertEquals { expected = sb; actual = sb2; };
}

void testIStringBox() {
    IStringBox sb = makeIStringBox("s1");
    assertEquals { expected = "s1"; actual = sb.s; };
    sb.setS("s2");
    assertEquals { expected = "s2"; actual = sb.getS(); };
    sb.s = "s3";
    assertEquals { expected = "s3"; actual = sb.getS(); };
    sb.setS("s4");
    assertEquals { expected = "s4"; actual = sb.s; };
    IStringBox sb2 = sb.self();
    assertEquals { expected = sb; actual = sb2; };
}

void testNonConstEnum() {
    for (exp->act in {
            0->NonConstEnum.zero,
            1->NonConstEnum.one,
            2->NonConstEnum.two,
            3->NonConstEnum.three,
            4->NonConstEnum.four,
            42->NonConstEnum.fortyTwo,
            13.37->NonConstEnum.thirteenDotThreeSeven,
            -1->NonConstEnum.minusOne,
            8->NonConstEnum.eight,
            12->NonConstEnum.twelve,
            1000->NonConstEnum.counterFromThousand
        }) {
        assertEquals { expected = exp of Object; actual = act of Object; };
        assert ((exp of Anything) is NonConstEnum);
        assert ((act of Anything) is NonConstEnum);
    }
    incrementNonConstEnumCounter();
    assertEquals { expected = 1001; actual = NonConstEnum.counterFromThousand; };
}

void testConstEnum() {
    for (exp->act in {
            0->ConstEnum.zero,
            1->ConstEnum.one,
            2->ConstEnum.two,
            3->ConstEnum.three,
            4->ConstEnum.four,
            42->ConstEnum.fortyTwo,
            13.37->ConstEnum.thirteenDotThreeSeven,
            -1->ConstEnum.minusOne,
            8->NonConstEnum.eight,
            12->NonConstEnum.twelve
        }) {
        assertEquals { expected = exp of Object; actual = act of Object; };
        assert ((exp of Anything) is ConstEnum);
        assert ((act of Anything) is ConstEnum);
    } 
}

void testNumberOrStringIdentity() {
    Float|Integer|String fis0 = numberOrStringIdentity(0);
    Float|Integer|String fis1337 = numberOrStringIdentity(13.37);
    Float|Integer|String fisA = numberOrStringIdentity("A");
    assertEquals { expected = 0; actual = fis0; };
    assertEquals { expected = 13.37; actual = fis1337; };
    assertEquals { expected = "A"; actual = fisA; };
}

void testGetAB() {
    HasStringA&HasNumberB ab = getAB("a", 2);
    assertEquals { expected = "a"; actual = ab.a; };
    assertEquals { expected = 2; actual = ab.b; };
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
    testIStringBox();
    testNonConstEnum();
    testConstEnum();
    testNumberOrStringIdentity();
    testGetAB();
}

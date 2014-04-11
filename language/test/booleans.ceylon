@test
shared void booleans() {
    check(true==true, "boolean equals");
    check(true===true, "boolean identical");
    check(false!=true, "boolean not equals");
    check(true.string=="true", "true string");
    check(false.string=="false", "false string");
    check((1==1)==true, "boolean equals");
    check((1==2)==false, "boolean equals");
    check(true!="true", "boolean equals");
    Object falseObj = false;
    check(false == falseObj, "boolean equals");
    check(true != falseObj, "boolean not equals");
    
    check(true.equals(true), "boolean equals");
    check(!true.equals("true"), "boolean equals");
    
    if (true) {}
    
    if (true) {
    }
    else {
        fail("if true");
    }
    if (false) {
        fail("if false");
    }
    while (false&&1==1) {
        fail("while false");
    }
    
    if (false) {
       fail("if false");
    }
    else {}
    
    while (true) { break; }
    
    function obj(Object o) {
        return o;
    }
    function bool(Boolean b) {
        return b;
    }
    Object o=obj(true);
    Boolean b=bool(false);
    
    /*Boolean bt = true;
    switch (bt)
    case (true) {}
    case (false) { fail("boolean switch"); }*/
    check(parseBoolean("true") exists, "parseBoolean(true)");
    check(parseBoolean("false") exists,"parseBoolean(false)");
    check(!parseBoolean("Yes") exists, "parseBoolean(Yes)");
    
    check(false == true.not, "true.not");
    check(true == false.not, "false.not");
    
    check(true == (true and true), "true and true");
    check(false == (true and false), "true and false");
    check(false == (false and true), "false and true");
    check(false == (false and false), "false and false");
    
    check(true == (true or true), "true or true");
    check(true == (true or false), "true or false");
    check(true == (false or true), "false or true");
    check(false == (false or false), "false or false");
    
    check(false == (true xor true), "true xor true");
    check(true == (true xor false), "true xor false");
    check(true == (false xor true), "false xor true");
    check(false == (false xor false), "false xor false");
    
    check(false == true.clear(0), "true.clear(0)");
    check(false == false.clear(0), "false.clear(0)");
    
    check(false == true.flip(0), "true.flip(0)");
    check(true == false.flip(0), "false.flip(0)");
    
    check(true == true.get(0), "true.get(0)");
    check(false == false.get(0), "false.get(0)");
    
    check(true == true.set(0), "true.set(0)");
    check(true == false.set(0), "false.set(0)");
    check(false == true.set(0, false), "true.set(0, false)");
    check(false == false.set(0, false), "false.set(0, false)");
    
    check(true == true.leftLogicalShift(-2), "true.leftLogicalShift(-2)");
    check(true == true.leftLogicalShift(-1), "true.leftLogicalShift(-1)");
    check(true == true.leftLogicalShift(0), "true.leftLogicalShift(0)");
    check(true == true.leftLogicalShift(1), "true.leftLogicalShift(1)");
    check(true == true.leftLogicalShift(2), "true.leftLogicalShift(2)");
    
    check(false == false.leftLogicalShift(-2), "false.leftLogicalShift(-2)");
    check(false == false.leftLogicalShift(-1), "false.leftLogicalShift(-1)");
    check(false == false.leftLogicalShift(0), "false.leftLogicalShift(0)");
    check(false == false.leftLogicalShift(1), "false.leftLogicalShift(1)");
    check(false == false.leftLogicalShift(2), "false.leftLogicalShift(2)");
    
    check(true == true.rightLogicalShift(-2), "true.rightLogicalShift(-2)");
    check(true == true.rightLogicalShift(-1), "true.rightLogicalShift(-1)");
    check(true == true.rightLogicalShift(0), "true.rightLogicalShift(0)");
    check(true == true.rightLogicalShift(1), "true.rightLogicalShift(1)");
    check(true == true.rightLogicalShift(2), "true.rightLogicalShift(2)");
    
    check(false == false.rightLogicalShift(-2), "false.rightLogicalShift(-2)");
    check(false == false.rightLogicalShift(-1), "false.rightLogicalShift(-1)");
    check(false == false.rightLogicalShift(0), "false.rightLogicalShift(0)");
    check(false == false.rightLogicalShift(1), "false.rightLogicalShift(1)");
    check(false == false.rightLogicalShift(2), "false.rightLogicalShift(2)");
    
    check(true == true.rightArithmeticShift(-2), "true.rightArithmeticShift(-2)");
    check(true == true.rightArithmeticShift(-1), "true.rightArithmeticShift(-1)");
    check(true == true.rightArithmeticShift(0), "true.rightArithmeticShift(0)");
    check(true == true.rightArithmeticShift(1), "true.rightArithmeticShift(1)");
    check(true == true.rightArithmeticShift(2), "true.rightArithmeticShift(2)");
    
    check(false == false.rightArithmeticShift(-2), "false.rightArithmeticShift(-2)");
    check(false == false.rightArithmeticShift(-1), "false.rightArithmeticShift(-1)");
    check(false == false.rightArithmeticShift(0), "false.rightArithmeticShift(0)");
    check(false == false.rightArithmeticShift(1), "false.rightArithmeticShift(1)");
    check(false == false.rightArithmeticShift(2), "false.rightArithmeticShift(2)");
    
    Boolean allOnes = true;
    Boolean allZeros = false;
    Boolean leftmostOne = true;
    Boolean rightmostOne = true;
    value oobIndices = (-2..-1).chain(1..2);
    // by doing the tests using values of the real type and on type parameters
    // we test both jvm optimized and non-optimized paths
    void binaryOob<T>(T ones, T zeros, T leftmost, T rightmost)
            given T satisfies Binary<T> {
        for (oobIndex in oobIndices) {
            value ibIndex = 0;
            for (lhs in [ones, zeros, leftmost, rightmost]) {
                // Theses are weirdly circular
                check(lhs.leftLogicalShift(oobIndex) == lhs.leftLogicalShift(ibIndex), "``lhs``.leftLogicalShift(``oobIndex``) == ``lhs``.leftLogicalShift(``ibIndex``)");
                check(lhs.rightLogicalShift(oobIndex) == lhs.rightLogicalShift(ibIndex), "``lhs``.rightLogicalShift(``oobIndex``) == ``lhs``.leftLogicalShift(``ibIndex``)");
                check(lhs.rightArithmeticShift(oobIndex) == lhs.rightArithmeticShift(ibIndex), "``lhs``.leftLogicalShift(``oobIndex``) == ``lhs``.rightArithmeticShift(``ibIndex``)");
                // These should be noops
                check(lhs.clear(oobIndex) == lhs,      "``lhs``.clear(``oobIndex``) == ``lhs``");
                check(lhs.flip(oobIndex) == lhs,       "``lhs``.flip(``oobIndex``) == ``lhs``");
                check(lhs.set(oobIndex) == lhs,        "``lhs``.set(``oobIndex``) == ``lhs``");
                check(lhs.set(oobIndex, false) == lhs, "``lhs``.set(``oobIndex``, false) == ``lhs``");
                // This should be false
                check(lhs.get(oobIndex) == false,      "``lhs``.get(``oobIndex``) == false");
            }
            for (lhs in [allOnes, allZeros, leftmostOne, rightmostOne]) {
                // Theses are weirdly circular
                check(lhs.leftLogicalShift(oobIndex) == lhs.leftLogicalShift(ibIndex), "``lhs``.leftLogicalShift(``oobIndex``) == ``lhs``.leftLogicalShift(``ibIndex``)");
                check(lhs.rightLogicalShift(oobIndex) == lhs.rightLogicalShift(ibIndex), "``lhs``.rightLogicalShift(``oobIndex``) == ``lhs``.leftLogicalShift(``ibIndex``)");
                check(lhs.rightArithmeticShift(oobIndex) == lhs.rightArithmeticShift(ibIndex), "``lhs``.leftLogicalShift(``oobIndex``) == ``lhs``.rightArithmeticShift(``ibIndex``)");
                // These should be noops
                check(lhs.clear(oobIndex) == lhs,      "``lhs``.clear(``oobIndex``) == ``lhs``");
                check(lhs.flip(oobIndex) == lhs,       "``lhs``.flip(``oobIndex``) == ``lhs``");
                check(lhs.set(oobIndex) == lhs,        "``lhs``.set(``oobIndex``) == ``lhs``");
                check(lhs.set(oobIndex, false) == lhs, "``lhs``.set(``oobIndex``, false) == ``lhs``");
                // This should be false
                check(lhs.get(oobIndex) == false,      "``lhs``.get(``oobIndex``) == false");
            }
        }
    }
    binaryOob(allOnes, allZeros, leftmostOne, rightmostOne);
}

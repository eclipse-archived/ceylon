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
    
    check(false == true.not);
    check(true == false.not);
    
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
    
    check(false == true.clear(0));
    check(false == false.clear(0));
    
    check(false == true.flip(0));
    check(true == false.flip(0));
    
    check(true == true.get(0));
    check(false == false.get(0));
    
    check(true == true.set(0));
    check(true == false.set(0));
    check(false == true.set(0, false));
    check(false == false.set(0, false));
    
    check(true == true.leftLogicalShift(-2));
    check(true == true.leftLogicalShift(-1));
    check(true == true.leftLogicalShift(0));
    check(true == true.leftLogicalShift(1));
    check(true == true.leftLogicalShift(2));
    
    check(false == false.leftLogicalShift(-2));
    check(false == false.leftLogicalShift(-1));
    check(false == false.leftLogicalShift(0));
    check(false == false.leftLogicalShift(1));
    check(false == false.leftLogicalShift(2));
    
    check(true == true.rightLogicalShift(-2));
    check(true == true.rightLogicalShift(-1));
    check(true == true.rightLogicalShift(0));
    check(true == true.rightLogicalShift(1));
    check(true == true.rightLogicalShift(2));
    
    check(false == false.rightLogicalShift(-2));
    check(false == false.rightLogicalShift(-1));
    check(false == false.rightLogicalShift(0));
    check(false == false.rightLogicalShift(1));
    check(false == false.rightLogicalShift(2));
    
    check(true == true.rightArithmeticShift(-2));
    check(true == true.rightArithmeticShift(-1));
    check(true == true.rightArithmeticShift(0));
    check(true == true.rightArithmeticShift(1));
    check(true == true.rightArithmeticShift(2));
    
    check(false == false.rightArithmeticShift(-2));
    check(false == false.rightArithmeticShift(-1));
    check(false == false.rightArithmeticShift(0));
    check(false == false.rightArithmeticShift(1));
    check(false == false.rightArithmeticShift(2));
}

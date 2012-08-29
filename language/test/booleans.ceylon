shared void booleans() {
    check(true==true, "boolean equals");
    check(true===true, "boolean identical");
    check(false!=true, "boolean not equals");
    check(!false===true, "boolean not identical");
    check(true.string=="true", "true string");
    check(false.string=="false", "false string");
    check((1==1)==true, "boolean equals");
    check((1==2)==false, "boolean equals");
    check(true!="true", "boolean equals");
    
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
    
}

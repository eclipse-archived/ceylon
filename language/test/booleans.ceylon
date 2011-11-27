shared void booleans() {
    assert(true==true, "boolean equals");
    assert(true===true, "boolean identical");
    assert(false!=true, "boolean not equals");
    assert(!false===true, "boolean not identical");
    assert(true.string=="true", "true string");
    assert(false.string=="false", "false string");
    assert((1==1)==true, "boolean equals");
    assert((1==2)==false, "boolean equals");
    assert(true!="true", "boolean equals");
    
    assert(true.equals(true), "boolean equals");
    assert(!true.equals("true"), "boolean equals");
    
    if (true) {
    }
    else {
        fail("if true");
    }
    if (false) {
        fail("if false");
    }
    while (false) {
        fail("while false");
    }
    
    function obj(Object o) {
        return o;
    }
    function bool(Boolean b) {
        return b;
    }
    Object o=obj(true);
    Boolean b=bool(false);
}
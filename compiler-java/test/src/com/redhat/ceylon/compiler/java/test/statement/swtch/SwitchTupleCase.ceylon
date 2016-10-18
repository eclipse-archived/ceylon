class SwitchTupleCase() {
    /*void example() {
        Integer[2] pair = [1, 2];
        Integer y = switch (pair) case ([1, 2]) 1 else 0;
        
        [Float, Float*] floats = [1.0, 2.0];
        Float x = switch (floats) case ([Float x, Float* ys]) x+(ys[0] else 0.0);
    }*/
    
    shared String statement([String*] strings) {
        switch(strings)
        case ([]) { return "empty"; }
        case (["a", "b"]) { return "ab"; }
        else { return "other"; }
    }
    shared String expression([String*]? strings) 
        =>  switch(strings)
        case ([])   "empty"
        case (["a", "b"]) "ab"
        case (null)   "null"
        else "other";
}

void switchTupleCase() {
    value stc = SwitchTupleCase();
    assert (stc.statement([]) == "empty"); 
    assert (stc.statement(["a", "b"]) == "ab");
    assert (stc.statement({"a", "b"}.sequence()) == "other");
    assert (stc.statement(["c", "d"]) == "other");
    assert (stc.expression([]) == "empty"); 
    assert (stc.expression(["a", "b"]) == "ab");
    assert (stc.expression({"a", "b"}.sequence()) == "other");
    assert (stc.expression(["c", "d"]) == "other");
}
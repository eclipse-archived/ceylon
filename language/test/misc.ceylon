void misc() {
    
    function stringify(Character... chars) {
        StringBuilder sb = StringBuilder();
        for (c in chars) {
            sb.appendCharacter(c);
        }
        return sb.string;
    }
    
    assert(stringify("hello".characters...)=="hello", "args");
    assert(stringify( `h`, `i` )=="hi", "sequenced args");
    assert(stringify { chars="hello".characters; }=="hello", "named args");
    assert(stringify { `h`, `i` }=="hi", "named sequenced args");
    assert(stringify()=="", "no args");
    assert(stringify{}=="", "no named args");
            
    variable Natural? x := 0;
    while (exists y = x) { 
        x := null; 
    }
    assert(!exists x, "while exists");
    
}
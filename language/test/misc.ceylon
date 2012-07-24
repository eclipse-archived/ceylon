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
            
    variable Integer? x := 0;
    while (exists y = x) { 
        x := null; 
    }
    assert(!exists x, "while exists");
    
    variable value s := "hello";
    while (nonempty chars = s.characters) { 
        s:=""; 
    }
    assert(s=="", "while nonempty");
    
    value bs = SequenceBuilder<Integer>();
    for (i in 0..10) {
        assert(bs.size==i, "builder size");
        bs.append(i);
    }
    assert(bs.size==11, "builder size");
    
    for (n->e in entries(bs.sequence...)) {
        assert(n==e, "entry iteration");
    }
    
    see(); by(); tagged();
    every(); any(); count(); first();
    join(); entries(); elements();
    array(); coalesce(); string();
}

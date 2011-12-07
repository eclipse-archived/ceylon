void characters() {
    value c = `a`;
    assert(c.lowercase, "lowercase char");
    assert(!c.uppercase, "lowercase char");
    assert(`A`.uppercase, "uppercase char");
    assert(!`A`.lowercase, "uppercase char");
    assert(c.uppercased==`A`, "uppercased char");
    assert(`A`.lowercased==`a`, "lowercased char");
    assert(c.string=="a", "character string");
    assert(!c.whitespace, "character not whitespace");
    assert(!c.digit, "character not whitespace");
    assert(` `.whitespace, "character whitespace");
    assert(`1`.digit, "character digt");
    assert(`a`<`z`, "char order");
    assert(`a`.successor==`b`, "char successor");
    assert(`Z`.predecessor==`Y`, "char predecessor");
    
    variable value i:=0;
    for (x in `a`..`z`) {
        i:=i+1;
        assert(x>=`a`&&x<=`z`, "character range");
    }
    assert(i==26, "character range");    
}
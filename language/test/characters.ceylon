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
    assert(`\t`.integer==9, "escaped characters 1");
    assert(`\\`.integer==92, "escaped characters 2");
    assert(`\``.integer==96, "escaped characters 3");
    assert(`\n`.integer==10, "escaped characters 4");
    assert(`\"`.integer==34, "escaped characters 5");
    assert(`\{00E5}`.integer==229, "Unicode escape");
    
    variable value i:=0;
    for (x in `a`..`z`) {
        i:=i+1;
        assert(x>=`a`&&x<=`z`, "character range");
    }
    assert(i==26, "character range");   
    
    assert(c.integer.character==c, "integer/character conversion");
    assert(69.character.integer==69, "integer/character conversion");
     
}

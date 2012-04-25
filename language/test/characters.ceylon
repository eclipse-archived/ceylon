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
    assert(`\b`.integer==8,  "escaped chars 1");
    assert(`\t`.integer==9,  "escaped chars 2");
    assert(`\n`.integer==10, "escaped chars 3");
    assert(`\f`.integer==12, "escaped chars 4");
    assert(`\r`.integer==13, "escaped chars 5");
    assert(`\\`.integer==92, "escaped chars 6");
    assert(`\``.integer==96, "escaped chars 7");
    assert(`\"`.integer==34, "escaped chars 8");
    assert(`\'`.integer==39, "escaped chars 9");
    assert(`\{00E5}`.integer==229, "Unicode escape 1");
    assert(`\{0008}`==`\b`, "Unicode escape 2");
    assert(`\{0009}`==`\t`, "Unicode escape 3");
    assert(`\{000A}`==`\n`, "Unicode escape 4");
    assert(`\{000C}`==`\f`, "Unicode escape 5");
    assert(`\{000D}`==`\r`, "Unicode escape 6");
    assert(`\{005C}`==`\\`, "Unicode escape 7");
    //print(`\{005C}`.integer);
    assert(`\{0060}`==`\``, "Unicode escape 8");
    assert(`\{0022}`==`\"`, "Unicode escape 9");
    assert(`\{0027}`==`\'`, "Unicode escape 10");
    
    variable value i:=0;
    for (x in `a`..`z`) {
        i:=i+1;
        assert(x>=`a`&&x<=`z`, "character range");
    }
    assert(i==26, "character range");   
    
    assert(c.integer.character==c, "integer/character conversion");
    assert(69.character.integer==69, "integer/character conversion");
     
}

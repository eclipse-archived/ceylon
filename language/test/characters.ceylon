@test
shared void characters() {
    value c = 'a';
    check(c.lowercase, "lowercase char");
    check(!c.uppercase, "lowercase char");
    check('A'.uppercase, "uppercase char");
    check(!'A'.lowercase, "uppercase char");
    check(c.uppercased=='A', "uppercased char");
    check('A'.lowercased=='a', "lowercased char");
    check(c.string=="a", "character string");
    check(!c.whitespace, "character not whitespace");
    check(!c.digit, "character not whitespace");
    check(' '.whitespace, "character whitespace");
    check('1'.digit, "character digt");
    check('a'<'z', "char order");
    check('a'.successor=='b', "char successor");
    check('Z'.predecessor=='Y', "char predecessor");
    check('\b'.integer==8,  "escaped chars 1");
    check('\t'.integer==9,  "escaped chars 2");
    check('\n'.integer==10, "escaped chars 3");
    check('\f'.integer==12, "escaped chars 4");
    check('\r'.integer==13, "escaped chars 5");
    check('\\'.integer==92, "escaped chars 6");
    check('\`'.integer==96, "escaped chars 7");
    check('\"'.integer==34, "escaped chars 8");
    check('\''.integer==39, "escaped chars 9");
    check('\{#00E5}'.integer==229, "Unicode escape 1");
    check('\{#0008}'=='\b', "Unicode escape 2");
    check('\{#0009}'=='\t', "Unicode escape 3");
    check('\{#000A}'=='\n', "Unicode escape 4");
    check('\{#000C}'=='\f', "Unicode escape 5");
    check('\{#000D}'=='\r', "Unicode escape 6");
    check('\{#005C}'=='\\', "Unicode escape 7");
    //print('\{#005C}'.integer);
    check('\{#0060}'=='\`', "Unicode escape 8");
    check('\{#0022}'=='\"', "Unicode escape 9");
    check('\{#0027}'=='\'', "Unicode escape 10");
    
    variable value i=0;
    for (x in 'a'..'z') {
        i=i+1;
        check(x>='a'&&x<='z', "character range");
    }
    check(i==26, "character range");   
    
    check(c.integer.character==c, "integer/character conversion");
    check(69.character.integer==69, "integer/character conversion");

    check('a'.largerThan('A'), "Character.largerThan");
    check('A'.smallerThan('a'), "Character.smallerThan");
    check('A'.notLargerThan('B'), "Character.notLargerThan");
    check('B'.notSmallerThan('A'), "Character.notSmallerThan");
    check('A'.neighbour(2)=='C', "Character.neighbour 1");
    check('Z'.neighbour(-2)=='X', "Character.neighbour 2");
}

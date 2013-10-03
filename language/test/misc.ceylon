@test
by()
//see()
tagged()
shared void misc() {
    
    function stringify(Character* chars) {
        StringBuilder sb = StringBuilder();
        for (c in chars) {
            sb.appendCharacter(c);
        }
        return sb.string;
    }
    
    check(stringify(*"hello".characters)=="hello", "args");
    check(stringify( 'h', 'i' )=="hi", "sequenced args");
    //unusable check(stringify { chars="hello".characters; }=="hello", "named args");
    // FIXME: Disabled until we fix the backend
    //check(stringify { chars=['h', 'i']; }=="hi", "named sequenced args");
    check(stringify()=="", "no args");
    check(stringify{}=="", "no named args");
            
    variable Integer? x = 0;
    while (exists y = x) { 
        x = null; 
    }
    check(!x exists, "while exists");
    
    variable value s = "hello";
    while (nonempty chars = s.sequence) { 
        s=""; 
    }
    check(s=="", "while nonempty");
    
    value bs = SequenceBuilder<Integer>();
    for (i in 0..10) {
        check(bs.size==i, "builder size");
        bs.append(i);
    }
    check(bs.size==11, "builder size");
    
    for (n->e in entries(bs.sequence)) {
        check(n==e, "entry iteration");
    }
    
    //Test empty varargs
    //see(); 
    by(); tagged();
    concatenate();
    ",".join{};
    StringBuilder().appendAll{};
    //LazyList<Nothing>(); LazyMap<Nothing,Nothing>(); LazySet<Nothing>();
    [1,2,3].items([]);
    [1,2,3].definesAny([]);
    [1,2,3].definesEvery([]);
    [1,2,3].containsAny([]);
    [1,2,3].containsEvery([]);
    check(first{1,null,3} exists, "first [1]");
    check(!first{null,2,3} exists, "first [2]");
    check(!first{null,null,3} exists, "first [3]");
    {Integer*} noints={};
    check(!first(noints) exists, "first [4]");
    print(null);
}

by()
see()
tagged()
void misc() {
    
    function stringify(Character... chars) {
        StringBuilder sb = StringBuilder();
        for (c in chars) {
            sb.appendCharacter(c);
        }
        return sb.string;
    }
    
    check(stringify("hello".characters...)=="hello", "args");
    check(stringify( `h`, `i` )=="hi", "sequenced args");
    //unusable check(stringify { chars="hello".characters; }=="hello", "named args");
    check(stringify { chars=`h`; chars=`i`; }=="hi", "named sequenced args");
    check(stringify()=="", "no args");
    check(stringify{}=="", "no named args");
            
    variable Integer? x := 0;
    while (exists y = x) { 
        x := null; 
    }
    check(!x exists, "while exists");
    
    variable value s := "hello";
    while (nonempty chars = s.characters) { 
        s:=""; 
    }
    check(s=="", "while nonempty");
    
    value bs = SequenceBuilder<Integer>();
    for (i in 0..10) {
        check(bs.size==i, "builder size");
        bs.append(i);
    }
    check(bs.size==11, "builder size");
    
    for (n->e in entries(bs.sequence...)) {
        check(n==e, "entry iteration");
    }

    //Test empty varargs
    see(); by(); tagged();
    any(); array(); coalesce(); count();
    elements(); entries(); every(); first();
    join(); string();
    ",".join(); sort();
    StringBuilder().appendAll();
    SequenceBuilder().appendAll();
    //LazyList<Bottom>(); LazyMap<Bottom,Bottom>(); LazySet<Bottom>();
    {1,2,3}.items();
    {1,2,3}.definesAny();
    {1,2,3}.definesEvery();
    {1,2,3}.containsAny();
    {1,2,3}.containsEvery();
    print(null);
}

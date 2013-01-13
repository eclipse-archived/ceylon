void comprehensions() {
    
    {String*} words = { "hello", "world", "goodbye" };
    String?[] wordsAnNulls = [ "hello", "world", null, "goodbye" ];
    
    [String*] eager = [ for (w in words) if (w.size>2) w ];
    {String*} lazy = { for (w in words) if (w.size>2) w };
    
    String joined1 = " ".join(for (w in wordsAnNulls) if (exists w) w.uppercased);
    String joined2 = " ".join { @error for (w in wordsAnNulls) if (exists w) w.trimmed };
    Entry<String,Integer>[] entries = [ for (s in words) for (n in 0..10) s->n ];
    @type:"Sequential<String>" value seq = [ for (x in {null, "hello", "goodbye"}) if (exists x) if (x.size>1) x.uppercased ];
    @type:"Iterable<String,Null>" value iter = { for (x in {null, "hello", "goodbye"}) if (exists x) if (x.size>1) x.uppercased };
    @type:"Array<String>" array(for (x in {null, "hello", "goodbye"}) if (exists x) if (x.size>1) x.uppercased);
    
    value yyy = { *array (*seq) };
    value xxx = { *array { @error *seq } };
    
    value nulls = coalesce(for (c in "hElLo") null).sequence;
    value nullsAndChars = coalesce(for (c in "hElLo") c.uppercase then c else null).sequence;
    value nulls2 = {for (c in "hElLo") null}.coalesced;
    value nullsAndChars2 = {for (c in "hElLo") c.uppercase then c else null}.coalesced;
        
    [String*] eagerWithStatic = [ "hello", "world", for (w in words) if (w.size>2) w ];
    {String*} lazyWithStatic = { "hello", "world", for (w in words) if (w.size>2) w };
    @type:"Tuple<Integer|Float|String,Integer,Tuple<Float|String,Float,Sequential<String>>>" value eagerWithStaticMixed = [ 1, 1.0, for (w in words) if (w.size>2) w ];
    @type:"Iterable<Integer|Float|String,Nothing>" value lazyWithStaticMixed = { 2, 2.0, for (w in words) if (w.size>2) w };
    
    [String*] eagerWithStaticAndSpread = [ "hello", "world", *eager ];
    {String*} lazyWithStaticAndSpread = { "hello", "world", *lazy };
    @type:"Tuple<Integer|Float|String,Integer,Tuple<Float|String,Float,Sequential<String>>>" value eagerWithStaticAndSpreadMixed = [ 1, 1.0, *eager ];
    @type:"Iterable<Integer|Float|String,Nothing>" value lazyWithStaticAndSpreadMixed = { 2, 2.0, *lazy };
    
    [String*] eagerBroken = [ @error "hello", for (w in words) if (w.size>2) w, "world" ];
    {String*} lazyBroken = { @error "hello", for (w in words) if (w.size>2) w, "world" };
    [String*] eagerWithStaticAndSpreadBroken = [ @error "hello", *eager, "world" ];
    {String*} lazyWithStaticAndSpreadBroken = { @error "hello", *lazy, "world" };
    
    function variadic(String* strings) => strings.size;
    function iterated({String*} strings) => strings.size;
    
    value pos = variadic( for (w in words) if (w.size>2) w );
    value posWithStatic = variadic( "hello", "world", for (w in words) if (w.size>2) w );
    value posWithStaticAndSpread = variadic( "hello", "world", *eager );
    @error value posWithStaticError1 = variadic( "hello", 1, for (w in words) if (w.size>2) w );
    @error value posWithStaticError2 = variadic( "hello", "world", for (w in words) if (w.size>2) w.size );
    
    value variadicRef = variadic;
    value posRef = variadicRef( for (w in words) if (w.size>2) w );
    value posWithStaticRef = variadicRef( "hello", "world", for (w in words) if (w.size>2) w );
    value posWithStaticAndSpreadRef = variadicRef( "hello", "world", *eager );    
    @error value posWithStaticError1Ref = variadicRef( "hello", 1, for (w in words) if (w.size>2) w );
    @error value posWithStaticError2Ref = variadicRef( "hello", "world", for (w in words) if (w.size>2) w.size );
    
    value named = iterated { for (w in words) if (w.size>2) w };
    value namedWithStatic = iterated { "hello", "world", for (w in words) if (w.size>2) w };
    value namedWithStaticAndSpread = iterated { "hello", "world", *lazy };
    value namedWithStaticError1 = iterated { @error "hello", 1, for (w in words) if (w.size>2) w };
    value namedWithStaticError2 = iterated { @error "hello", "world", for (w in words) if (w.size>2) w.size };
    
}
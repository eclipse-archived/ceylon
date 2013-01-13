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
        
    //[String*] eagerWithStatic = [ "hello", "world", for (w in words) if (w.size>2) w ];
    //{String*} lazyWithStatic = { "hello", "world", for (w in words) if (w.size>2) w };
    
    [String*] eagerWithStaticAndSpread = [ "hello", "world", *eager ];
    {String*} lazyWithStaticAndSpread = { "hello", "world", *lazy };
    
    //[String*] eagerBroken = [ "hello", for (w in words) if (w.size>2) w, "world" ];
    //{String*} lazyBroken = { "hello", for (w in words) if (w.size>2) w, "world" };
    
    function variadic(String* strings) => strings.size;
    function iterated({String*} strings) => strings.size;
    
    value pos = variadic( for (w in words) if (w.size>2) w );
    //value posWithStatic = variadic( "hello", "world", for (w in words) if (w.size>2) w );
    value posWithStaticAndSpread = variadic( "hello", "world", *eager );
    
    value named = iterated { for (w in words) if (w.size>2) w };
    //value namedWithStaic = iterated { "hello", "world", for (w in words) if (w.size>2) w };
    value namedWithStaticAndSpread = iterated { "hello", "world", *lazy };
    
}
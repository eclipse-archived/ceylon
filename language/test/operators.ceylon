void operators() {
    
    Ordinal<Integer>&Subtractable<Integer,Integer> t;
    
    String? maybe = "hello";
    String? maybeNot = null;
    assert(exists maybe?.uppercased, "?.");
    assert(!exists maybeNot?.uppercased, "?.");
    assert(maybe?"goodbye"=="hello", "?");
    assert(maybeNot?"goodbye"=="goodbye", "?");
    assert(exists maybe?[0], "?[]");
    assert(exists maybe?[4], "?[]");
    assert(!exists maybe?[10], "?[]");
    assert(!exists maybeNot?[0], "?[]");
    assert(!exists maybeNot?[10], "?[]");
    
    String[] empty = {};
    String[] full = { "hello", "world" };
    //assert(!nonempty empty[].uppercased, "[].");
    //assert(nonempty full[].uppercased, "[].");
    assert("hello" in "hello world", "in");
    assert("world" in "hello world", "in");
                
    Correspondence<Integer, String> c1 = {};
    assert(!exists c1[0], "empty correspondence");
    
    Ranged<Integer,String[]> sequence = {"foo", "bar"};
    String[] subrange = sequence[1..2];
    assert(subrange.size==1, "subrange size");
    assert(nonempty subrange, "subrange nonempty");
    assert(sequence[1...].size==1, "open subrange size");
    assert(sequence[0...].size==2, "open subrange size");
    assert(nonempty sequence[1...], "open subrange nonempty");
    assert(!nonempty sequence[2...], "open subrange empty");
                                
    Float x = 0.5;
    assert(exists (x>0.0 then x), "then not null");
    assert(!exists (x<0.0 then x), "then null");
    assert((x<0.0 then x else 1.0) == 1.0, "then else");
    assert((x>0.0 then x else 1.0) == 0.5, "then");
    
    assert((maybe else "goodbye")=="hello", "else");
    assert((maybeNot else "goodbye")=="goodbye", "else");
    
    class X() {}
    X? xx = X();
    assert(is X (xx else X()), "something");
    assert(is X (true then X()), "something");
    assert(is X (true then X() else X()), "something");

    variable Subtractable<Integer,Integer> iii1 := +1;
    iii1:=iii1-+1;
    iii1-=+1;    
    iii1 := iii1-=+1;
    variable Ordinal<Integer>&Subtractable<Integer,Integer> iii2 := +1;    
    iii2 := iii2-=-2;
}
void operators() {
    
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
    assert(!nonempty empty[].uppercased, "spread 1");
    assert(nonempty full[].uppercased, "spread 2");
    value spread1 = full[].uppercased;
    value spread2 = full[].item(1);
	if (exists s1s=spread1[0]) {
        assert(s1s == "HELLO", "spread 3");
    } else { fail("spread 3"); }
    if (exists s1s=spread1[1]) {
        assert(s1s == "WORLD", "spread 4");
    } else { fail("spread 4"); }
    assert(spread1.size == 2, "spread 5");
    assert(spread2.size == 2, "spread 6");
    if (exists s2s=spread2[0]) {
        assert(s2s == `e`, "spread 7");
    } else { fail("spread 7"); }
    if (exists s2s=spread2[1]) {
        assert(s2s == `o`, "spread 8");
    } else { fail("spread 8"); }
    /*Callable<Character?[], Integer> spread3 = full[].item;
    value spread4 = spread3(1);
    assert(spread4.size == 2, "spread 10");
    if (exists s4s=spread4[0]) {
        assert(s4s == "e", "spread 11");
    } else { fail("spread 11"); }
    if (exists s4s=spread4[1]) {
        assert(s4s == "o", "spread 12");
    } else { fail("spread 12"); }*/

    assert("hello" in "hello world", "in 1");
    assert("world" in "hello world", "in 2");

    Correspondence<Integer, String> c1 = {};
    assert(!exists c1[0], "empty correspondence");
    
    Ranged<Integer,String[]> sequence = {"foo", "bar"};
    String[] subrange = sequence[1..2];
    assert(subrange.size==1, "subrange size");
    assert(nonempty subrange, "subrange nonempty");
    assert(sequence[1...].size==1, "open subrange size 1");
    assert(sequence[0...].size==2, "open subrange size 2");
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
    Object? obj(Object? x) { return x; }
    assert(is X obj(xx else X()), "something");
    assert(is X obj(true then X()), "something");
    assert(is X obj(true then X() else X()), "something");

}

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
    //assert(!nonempty empty[].uppercased, "[].");
    //assert(nonempty full[].uppercased, "[].");
    assert("hello" in "hello world", "in");
    assert("world" in "hello world", "in");
                
    Correspondence<Natural, String> c1 = {};
    assert(!exists c1[0], "empty correspondence");
    
    Ranged<Natural,String[]> sequence = {"foo", "bar"};
    String[] subrange = sequence[1..2];
    assert(subrange.size==1, "subrange size");
    assert(nonempty subrange, "subrange nonempty");
    //print(sequence[1...]);
    
}
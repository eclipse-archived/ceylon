shared void strings() {
    value hello = "hello";
    
    assert(hello.size==5, "string size");
    assert("".size==0, "empty string size");
    
    if (exists c = hello[0]) {
        assert(c==`h`, "string first element value");
    }
    else {
        fail("string first element exists");
    }
    if (exists c = hello[hello.size-1]) {
        assert(c==`o`, "string last element value");
    }
    else {
        fail("string last element exists");
    }
    if (exists c = hello[hello.size]) {
        fail("string element out of range");
    }
    
    variable value i:=0;
    for (c in hello) {
        value hc = hello[i];
        if (exists hc) {
            assert(hc==c, "iterated string element");
        }
        i+=1;
    }
    assert(i==hello.size, "string iteration");
    for (c in "") {
        fail("empty string iteration");
    }
    
    assert("a"<"z", "string comparison");
    assert("abc"=="abc", "string equality");
    assert("hello"+" "+"world"=="hello world", "string concatenation");
    assert(hello.uppercased=="HELLO", "uppercase");
    assert(hello.uppercased.lowercased==hello, "lowercase");
    assert(hello.contains("ll"), "string contains");
    assert(!hello.contains("x"), "string contains");
    assert(hello.longerThan(4), "string longer than");
    assert(!hello.longerThan(6), "string longer than");
    assert(!hello.longerThan(5), "string longer than");
    assert(("  " + hello + "\n").trimmed==hello, "string trim");
    
    value occ = hello.firstOccurrence("ll");
    if (exists occ) {
        assert(occ==2, "string first occurrence");
    }
    else {
        fail("string first occurrence");
    }
    value nocc = hello.firstOccurrence("x");
    if (exists nocc) {
        fail("string no first occurrence");
    }
    
    value chars = hello.characters;
    if (exists c = chars.first) {
        assert(c==`h`, "string first character");
    }
    else {
        fail("string first character");
    }
    assert(chars.size==5, "string characters size");
    if (nonempty chars) {
        assert(chars.lastIndex==4, "string characters last index");
    }
    else {
        fail("string characters nonempty");
    }
    if (nonempty nochars = "".characters) {
        fail("string characters empty");
    }
    
    assert(hello.keys.contains(3), "string keys");
    assert(!hello.keys.contains(6), "string keys");
    assert(!hello.keys.contains(5), "string keys");
    //assert(hello.keys.contains(-1), "string keys");
    assert(hello.keys.contains(0), "string keys");
    
    assert(hello.hash==("HE"+"LLO").lowercased.hash, "string hash");
    
    value builder = StringBuilder();
    builder.append("hello");
    builder.appendCharacter(` `);
    builder.append("world");
    String s = builder.string;
    assert(s=="hello world", "string builder");
}
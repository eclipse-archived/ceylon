shared void strings() {
    value hello = "hello";
    
    assert(hello.size==5, "string size 1");
    assert("".size==0, "empty string size 2");
    assert(!exists "".lastIndex, "empty string last index");
    assert(!exists ""[0], "empty string first element exists");
    
    assert("abcd".size==4, "string size 3");
    
    assert(hello.span(1,3)=="ell", "string span 1");
    assert(hello.span(1,null)=="ello", "string span 2");
    assert(hello.segment(1,3)=="ell", "string segment 3");
    assert("".span(1,3)=="", "empty string span 1");
    assert("".span(1,null)=="", "empty string span 2");
    assert("".segment(1,3)=="", "empty string segment");
        
    assert(exists hello[0], "string first element exists 1");
    if (exists li = hello.lastIndex) {
        assert(exists hello[li], "string first element exists 2");
    }
    else {
        fail("string last index");
    }
    assert (!exists hello[hello.size], "string element not exists");
    if (exists c = hello[0]) {
        assert(c==`h`, "string first element value");
    }
    else {
        fail("string first element exists 3");
    }
    if (exists c = hello[hello.size.predecessor]) {
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
    assert(i==hello.size, "string iteration 1");
    for (c in "") {
        fail("empty string iteration 2");
    }
    
    assert("a"<"z", "string comparison");
    assert("abc"=="abc", "string equality");
    assert("hello"+" "+"world"=="hello world", "string concatenation");
    assert(hello.uppercased=="HELLO", "uppercase");
    assert(hello.uppercased.lowercased==hello, "lowercase 1");
    assert("GoodBye".lowercased=="goodbye", "lowercase 2");
    assert(hello.contains("ll"), "string contains 1");
    assert(!hello.contains("x"), "string contains 2");
    assert(hello.longerThan(4), "string longer than 1");
    assert(!hello.longerThan(6), "string longer than 2");
    assert(!hello.longerThan(5), "string longer than 3");
    assert(("  " + hello + "\n").trimmed==hello, "string trim");
    assert("hello\n    world\ngoodbye   everyone!".normalized=="hello world goodbye everyone!", "string normalize");
    
    assert(`l` in hello, "char in string");
    assert(!`x` in hello, "char not in string");
    assert("ell" in hello, "substring in string");
    assert(!"goodbye" in hello, "substring not in string");
                
    if (exists occ = hello.firstOccurrence("ll")) {
        assert(occ==2, "string first occurrence 1");
    }
    else {
        fail("string first occurrence 2");
    }
    if (exists nocc = hello.firstOccurrence("x")) {
        fail("string no first occurrence");
    }
    if (exists locc = "hello hello".lastOccurrence("hell")) {
        assert(locc==6, "string last occurrence 1");
    }
    else {
        fail("string last occurrence 2");
    }
        
    if (exists occ = hello.firstCharacterOccurrence(`l`)) {
        assert(occ==2, "string first occurrence 1");
    }
    else {
        fail("string first occurrence 2");
    }
    if (exists nocc = hello.firstCharacterOccurrence(`x`)) {
        fail("string no first occurrence");
    }
    if (exists locc = "hello hello".lastCharacterOccurrence(`h`)) {
        assert(locc==6, "string last occurrence 1");
    }
    else {
        fail("string last occurrence 2");
    }
        
    value chars = hello.characters;
    if (exists char = chars[0]) {
        assert(char==`h`, "string characters 1");
    }
    else {
        fail("string characters 2");
    }
    if (exists char = chars[3]) {
        assert(char==`l`, "string characters 3");
    }
    else {
        fail("string characters 4");
    }
    if (exists char = chars[5]) {
        fail("string characters 5");
    }
    if (exists c = chars.first) {
        assert(c==`h`, "string first character 1");
    }
    else {
        fail("string first character 2");
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
    
    assert(hello.keys.contains(3), "string keys 1");
    assert(!hello.keys.contains(6), "string keys 2");
    assert(!hello.keys.contains(5), "string keys 3");
    //assert(hello.keys.contains(-1), "string keys 4");
    assert(hello.keys.contains(0), "string keys 5");
    
    assert(hello.hash==("HE"+"LLO").lowercased.hash, "string hash");
    
    value builder = StringBuilder();
    assert(builder.string=="", "StringBuilder 1");
    builder.append("hello");
    assert(builder.string=="hello", "StringBuilder 2");
    builder.appendCharacter(` `);
    builder.append("world");
    String s = builder.string;
    assert(s=="hello world", "string builder 1");
    builder.appendAll();
    builder.appendAll(" ");
    builder.appendAll("goodbye", " ", "everyone");
    builder.appendSpace();
    assert(builder.string=="hello world goodbye everyone ", "string builder 2");
    
    assert("hello world".initial(0)=="", "string initial 1");
    assert("hello world".terminal(0)=="", "string terminal 1");
    assert("hello world".initial(1)=="h", "string initial 2");
    assert("hello world".terminal(1)=="d", "string terminal 2");
    assert("hello world".initial(5)=="hello", "string initial 3");
    assert("hello world".terminal(5)=="world", "string terminal 3");
    assert("hello world".initial(20)=="hello world", "string initial 4");
    assert("hello world".terminal(20)=="hello world", "string terminal 4");
    assert("hello world".initial(10)=="hello worl", "string initial 5");
    assert("hello world".terminal(10)=="ello world", "string terminal 5");
    assert("hello world".initial(11)=="hello world", "string initial 6");
    assert("hello world".terminal(11)=="hello world", "string terminal 6");
    
    assert(min({"abc", "xyz", "foo", "bar"})=="abc", "strings min");
    assert(max({"abc", "xyz", "foo", "bar"})=="xyz", "strings max");
    
    assert(", ".join()=="", "string join no strings");
    assert(", ".join("foo")=="foo", "string join one string");
    assert(", ".join("foo", "bar", "baz")=="foo, bar, baz", "string join");
    
    assert("hello world".startsWith(hello), "string starts with 1");
    assert("hello world".endsWith("world"), "string ends with 1");
    assert(!"Hello world".startsWith(hello), "string starts with 2");
    assert(!"hello World".endsWith("world"), "string ends with 2");
    assert(!"".startsWith(hello), "empty string starts with");
    assert(!"".endsWith("world"), "empty string ends with");
    
    assert(!exists "".split(null, true).first, "string split first 1");
    assert(exists "hello".split(null, true).first, "string split first 2");
    assert("hello world".split(null, true).first?""=="hello", "string split first 3");
    variable value count:=0;
    for (tok in "hello world goodbye".split(" ", true)) {
        count++;
        assert(tok.size>4, "string token");
    }
    assert(count==3, "string tokens");
    for (tok in "  ".split(" ", true)) {
        fail("no string tokens");
    }
    
    assert("".reversed=="", "string reversed 1");
    assert("x".reversed=="x", "string reversed 2");
    assert(hello.reversed=="olleh", "string reversed 3");
    
    assert("hello".repeat(0)=="", "string repeat 1");
    assert("hello".repeat(1)=="hello", "string repeat 2");
    assert("hello".repeat(3)=="hellohellohello", "string repeat 3");
    
    assert("hello world".replace("hello","goodbye")=="goodbye world", "string replace");
    
    value nlb = StringBuilder();
    nlb.appendNewline();
    nlb.append("hello");
    nlb.appendNewline();
    assert(nlb.string.size==7, "string builder newline 1");
    assert(nlb.string=="\nhello\n", "string builder newline 2");

    value s1 = "as it should";
    value interp = "String part " 1 " interpolation " 2 " works" s1 "";
    assert(interp=="String part 1 interpolation 2 worksas it should", "String Interpolation");
}

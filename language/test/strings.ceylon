void iterate<Element>(List<Element> list) 
    given Element satisfies Object {
    for (element in list) {
        check(element in list);
    }
}

void compareIterables<T>(Iterable<T> aIterable, Iterable<T> bIterable, String message)
    given T satisfies Object {
    
    Iterator<T> bIterator = bIterable.iterator;
    for(T a in aIterable){
        T|Finished b = bIterator.next();
        check(b != finished, "``message``: Iterator B empty while expecting '``a``'");
        check(a == b, "``message``: Element '``a``' != '``b``'");
    }
    T|Finished b = bIterator.next();
    check(b == finished, "``message``: Iterator B not empty: extra '``b``'");
}

shared void strings() {
    value hello = "hello";
    
    check(hello.size==5, "string size 1");
    check("".size==0, "empty string size 2");
    check(!"".characters nonempty);
    check("a".characters nonempty);
    check(!"".lastIndex exists, "empty string last index");
    check(!""[0] exists, "empty string first element exists");
    check(!(hello of Object) is Identifiable, "string is Identifiable");
    
    check("abcd".size==4, "string size 3");
    
    check("http://foo.com".spanFrom(4)=="://foo.com", "string spanFrom 0");
    check("http://foo.com".spanTo(3)=="http", "string spanTo 0");
    check(hello.span(-2,-1)=="", "string span -2");
    check(hello.span(-2,0)=="h", "string span -1");
    check(hello.span(1,3)=="ell", "string span 1");
    check(hello.spanFrom(1)=="ello", "string spanFrom 2");
    check(hello.spanFrom(10)=="", "string spanFrom 3");
    check(hello.spanTo(1)=="he", "string spanTo 2");
    check(hello.spanTo(-1)=="", "string spanTo 3");
    check(hello.span(1,hello.size)=="ello", "string span 3");
    check(hello.span(1,10)=="ello", "string span 4");
    check(hello.span(2,1)=="le", "string span 5");
    check(hello.span(20,10)=="", "string span 6");
    
    check(hello.segment(1,3)=="ell", "string segment 1");
    check(hello.segment(1,5)=="ello", "string segment 2");
    check(hello.segment(1,0)=="", "string segment 3");
    check(hello.segment(1,10)=="ello", "string segment 4");
    check(hello.segment(10,20)=="", "string segment 5");
    
    check("".span(1,3)=="", "empty string span 1");
    check("".spanFrom(0)=="", "empty string spanFrom 0");
    check("".spanFrom(1)=="", "empty string spanFrom 1");
    check("".spanTo(0)=="", "empty string spanTo 0");
    check("".spanTo(1)=="", "empty string spanTo 1");
    check("".segment(1,3)=="", "empty string segment");
        
    check(hello[0] exists, "string first element exists 1");
    if (exists li = hello.lastIndex) {
        check(hello[li] exists, "string first element exists 2");
    }
    else {
        fail("string last index");
    }
    check(!hello[hello.size] exists, "string element not exists");
    if (exists c = hello[0]) {
        check(c=='h', "string first element value");
    }
    else {
        fail("string first element exists 3");
    }
    if (exists c = hello[hello.size.predecessor]) {
        check(c=='o', "string last element value");
    }
    else {
        fail("string last element exists");
    }
    if (exists c = hello[hello.size]) {
        fail("string element out of range");
    }
    
    variable value i=0;
    for (c in hello) {
        value hc = hello[i];
        if (exists hc) {
            check(hc==c, "iterated string element");
        }
        i+=1;
    }
    check(i==hello.size, "string iteration 1");
    for (c in "") {
        fail("empty string iteration 2");
    }
    
    check("a"<"z", "string comparison");
    check("abc"=="abc", "string equality");
    check("hello"+" "+"world"=="hello world", "string concatenation");
    check(hello.uppercased=="HELLO", "uppercase");
    check(hello.uppercased.lowercased==hello, "lowercase 1");
    check("GoodBye".lowercased=="goodbye", "lowercase 2");
    check(hello.contains("ll"), "string contains 1");
    check(!hello.contains("x"), "string contains 2");
    check(hello.longerThan(4), "string longer than 1");
    check(!hello.longerThan(6), "string longer than 2");
    check(!hello.longerThan(5), "string longer than 3");
    check(("  " + hello + "\n").trimmed==hello, "string trim");
    check("hello\n    world\ngoodbye   everyone!".normalized=="hello world goodbye everyone!", "string normalize");
    
    check('l' in hello, "char in string");
    check(!'x' in hello, "char not in string");
    check("ell" in hello, "substring in string");
    check(!"goodbye" in hello, "substring not in string");
                
    if (exists occ = hello.firstOccurrence("ll")) {
        check(occ==2, "string first occurrence 1");
    }
    else {
        fail("string first occurrence 2");
    }
    if (exists nocc = hello.firstOccurrence("x")) {
        fail("string no first occurrence");
    }
    if (exists locc = "hello hello".lastOccurrence("hell")) {
        check(locc==6, "string last occurrence 1");
    }
    else {
        fail("string last occurrence 2");
    }
        
    if (exists occ = hello.firstCharacterOccurrence('l')) {
        check(occ==2, "string first occurrence 1");
    }
    else {
        fail("string first occurrence 2");
    }
    if (exists nocc = hello.firstCharacterOccurrence('x')) {
        fail("string no first occurrence");
    }
    if (exists locc = "hello hello".lastCharacterOccurrence('h')) {
        check(locc==6, "string last occurrence 1");
    }
    else {
        fail("string last occurrence 2");
    }
        
    value chars = hello.characters;
    if (exists char = chars[0]) {
        check(char=='h', "string characters 1");
    }
    else {
        fail("string characters 2");
    }
    if (exists char = chars[3]) {
        check(char=='l', "string characters 3");
    }
    else {
        fail("string characters 4");
    }
    if (exists char = chars[5]) {
        fail("string characters 5");
    }
    if (exists c = chars.first) {
        check(c=='h', "string first character 1");
    }
    else {
        fail("string first character 2");
    }
    check(chars.size==5, "string characters size");
    if (nonempty chars) {
        check(chars.lastIndex==4, "string characters last index");
    }
    else {
        fail("string characters nonempty");
    }
    if (nonempty nochars = "".characters) {
        fail("string characters empty");
    }
    
    check(hello.keys.contains(3), "string keys 1");
    check(!hello.keys.contains(6), "string keys 2");
    check(!hello.keys.contains(5), "string keys 3");
    //check(hello.keys.contains(-1), "string keys 4");
    check(hello.keys.contains(0), "string keys 5");
    
    check(hello.hash==("HE"+"LLO").lowercased.hash, "string hash");
    
    value builder = StringBuilder();
    check(builder.string=="", "StringBuilder 1");
    builder.append("hello");
    check(builder.string=="hello", "StringBuilder 2");
    builder.appendCharacter(' ');
    builder.append("world");
    String s = builder.string;
    check(s=="hello world", "string builder 1");
    builder.appendAll();
    builder.appendAll(" ");
    builder.appendAll("goodbye", " ", "everyone");
    builder.appendSpace();
    check(builder.string=="hello world goodbye everyone ", "string builder 2");
    check(StringBuilder().append("a").appendCharacter('b')
        .appendAll("c", "d").appendSpace()
        .append("e").string=="abcd e",
        "string builder chained calls");
    check(builder.size == 29, "StringBuilder.size");
    check(builder.insert(5,',').insert(12,"!!!").string=="hello, world!!! goodbye everyone ", "StringBuilder.insert");
    check(builder.delete(12,3).delete(5,1).delete(99999,1).string=="hello world goodbye everyone ", "StringBuilder.delete 1");
    check(builder.delete(28,100).string=="hello world goodbye everyone", "StringBuilder.delete 2");
    check(builder.size==28, "StringBuilder.size 2");
    check(builder.reset().size==0, "StringBuilder.reset");

    check("hello world".initial(0)=="", "string initial 1");
    check("hello world".terminal(0)=="", "string terminal 1");
    check("hello world".initial(1)=="h", "string initial 2");
    check("hello world".terminal(1)=="d", "string terminal 2");
    check("hello world".initial(5)=="hello", "string initial 3");
    check("hello world".terminal(5)=="world", "string terminal 3");
    check("hello world".initial(20)=="hello world", "string initial 4");
    check("hello world".terminal(20)=="hello world", "string terminal 4");
    check("hello world".initial(10)=="hello worl", "string initial 5");
    check("hello world".terminal(10)=="ello world", "string terminal 5");
    check("hello world".initial(11)=="hello world", "string initial 6");
    check("hello world".terminal(11)=="hello world", "string terminal 6");
    
    check(min(["abc", "xyz", "foo", "bar"])=="abc", "strings min");
    check(max(["abc", "xyz", "foo", "bar"])=="xyz", "strings max");
    
    check(", ".join()=="", "string join no strings");
    check(", ".join("foo")=="foo", "string join one string");
    check(", ".join("foo", "bar", "baz")=="foo, bar, baz", "string join");
    check(",".join(for (c in "") c.string)=="", "string join empty comprehension");
    check(",".join(for (c in "A") c.string)=="A", "string join comprehension 1");
    check(",".join(for (c in "ABC") c.string)=="A,B,C", "string join comprehension 2");
    
    check("hello world".startsWith(hello), "string starts with 1");
    check("hello world".endsWith("world"), "string ends with 1");
    check(!"Hello world".startsWith(hello), "string starts with 2");
    check(!"hello World".endsWith("world"), "string ends with 2");
    check(!"".startsWith(hello), "empty string starts with");
    check(!"".endsWith("world"), "empty string ends with");
    
    check(!"".split((Character c) => c.whitespace, true).empty, "\"\".split((Character c) c.whitespace,true) is empty");
    check(!"hello".split((Character c) => c.whitespace, true).empty, "hello.split((Character c) c.whitespace,true) is empty");
    check("hello world".split((Character c) => c.whitespace, true).iterator.next()=="hello", "string split first 3.1");
    check("hello world".split(" ", true).iterator.next()=="hello", "string split first 3.2");
    check("hello world".split((Character c) => c==' ').sequence.size==2, "string split discarding [1]");
    check("hello world".split((Character c) => c==' ', false).sequence.size==3, "string split including [1]");
    check("hello world".split().sequence.size==2, "string split default");
    check("hello world".split((Character c) => c=='l', true).sequence.size==3, "string split discarding [2]");
    check("hello world".split("l", true).sequence.size==3, "string split discarding [3]");
    check("hello world".split((Character c) => c=='l', false).sequence.size==5, "string split including [2]");
    check("hello world".split((Character c) => c=='l', false, false).sequence=={"he","l","","l","o wor","l","d"}, "string split including [3]");
    check("hello world".split((Character c) => c=='l', false, true).sequence=={"he","ll","o wor", "l", "d"}, "string split including [4]");
    check("hello world".split("l", false, false).sequence=={"he","l","","l","o wor","l","d"}, "string split including [5]");
    check("hello world".split("l", false, true).sequence=={"he","ll","o wor", "l", "d"}, "string split including [6]");
    //With strings
    check("hello world".split("eo").sequence == "hello world".split({'e','o'}).sequence, "string split chars [1]");
    check("hello world".split("eo").sequence == "hello world".split(StringBuilder().append("o").append("e").string).sequence, "string split chars");
    variable value count=0;
    for (tok in "hello world goodbye".split()) {
        count++;
        check(tok.size>4, "string token");
    }
    check(count==3, "string tokens default");
    
    compareIterables({""}, "".split(), "Empty string");
    compareIterables({"", ""}, " ".split((Character c) => c==' ', true), "Two empty tokens");
    compareIterables({"", " ", ""}, " ".split((Character c) => c==' ', false), "Two empty tokens with WS");
    compareIterables({"hello", "world"}, "hello world".split((Character c) => c==' ', true), "Two parts");
    compareIterables({"", "hello", "world", ""}, " hello world ".split((Character c) => c==' ', true), "Two parts surounded with WS");
    compareIterables({"hello", " ", "world"}, "hello world".split((Character c) => c==' ', false), "Two parts with space token");
    compareIterables({"", " ", "hello", " ", "world", " ", ""}, " hello world ".split((Character c) => c==' ', false), "Two parts surounded with space tokens");
    compareIterables({"hello", "   ", "world"}, "hello   world".split((Character c) => c==' ', false), "Two parts with grouped space token");
    compareIterables({"", "  ", "hello", "   ", "world", "    ", ""}, "  hello   world    ".split((Character c) => c==' ', false), "Two parts surounded with grouped space tokens");
    compareIterables({"a", "b"}, "a/b".split((Character c) => c=='/', true, false), "a/b");
    compareIterables({"", "a", "b", ""}, "/a/b/".split((Character c) => c=='/', true, false), "/a/b/");
    compareIterables({"", "", "a", "", "b", "", ""}, "//a//b//".split((Character c) => c=='/', true, false), "//a//b//");
    compareIterables({"", "", "a", "", "b", "", ""}, "/?a/&b#/".split((Character c) => c in "/&#?", true, false), "/?a/&b#/ no tokens");
    compareIterables({"", "/", "", "?", "a", "/", "", "&", "b", "#", "", "/", ""}, "/?a/&b#/".split((Character c) => c in "/&#?", false, false), "/?a/&b#/ with tokens");
    compareIterables({"𐒄𐒅", "𐒁"}, "𐒄𐒅 𐒁".split((Character c) => c==' ', true), "High-surrogate Unicode string");
    compareIterables({"𐒄", "𐒁", ""}, "𐒄𐒅𐒁𐒕".split((Character c) => c in "𐒅𐒕", true), "High-surrogate Unicode delimiters");
    
    check("".reversed=="", "string reversed 1");
    check("x".reversed=="x", "string reversed 2");
    check(hello.reversed=="olleh", "string reversed 3");
    
    check("hello".repeat(0)=="", "string repeat 1");
    check("hello".repeat(1)=="hello", "string repeat 2");
    check("hello".repeat(3)=="hellohellohello", "string repeat 3");
    
    check("hello world".replace("hello","goodbye")=="goodbye world", "string replace");
    
    value nlb = StringBuilder();
    nlb.appendNewline();
    nlb.append("hello");
    nlb.appendNewline();

    iterate(hello);
    
    check(nlb.string.size==7, "string builder newline 1");
    check(nlb.string=="\nhello\n", "string builder newline 2");

    value s1 = "as it should";
    value interp = "String part `` 1 `` interpolation `` 2 `` works`` s1 ``";
    check(interp=="String part 1 interpolation 2 worksas it should", "String Interpolation");

    //Lines
    value mls = "a
                 b";
    value lines = mls.lines.iterator;
    check(mls.size==3, "multiline.size==3");
    if (is String _s=lines.next()) {
        check(_s=="a", "multiline.lines[0]=='a'");
    } else { fail("multiline.lines[0]"); }
    if (is String _s=lines.next()) {
        check(_s=="b", "multiline.lines[1]=='b'");
    } else { fail("multiline.lines[1]"); }
    check(mls.normalized=="a b", "multiline.normalized");

    //Occurrences
    value occurs = hello.occurrences("l").iterator;
    if (is Integer p=occurs.next()) {
        check(p==2, "occurrences[0]");
    } else { fail("occurrences 1"); }
    if (is Integer p=occurs.next()) {
        check(p==3, "occurrences[1]");
    } else { fail("occurrences 2"); }
    if (!occurs.next() is Finished) {
        fail("occurrences 3");
    }
    if (!hello.occurrences("X").iterator.next() is Finished) {
        fail("occurrences 4");
    }
    //Unicode escapes
    value ucs = "\{#00E5}";
    check(ucs.size==1, "Unicode escape 0");
    if (exists c=ucs[0]) {
        check(c.integer==229, "Unicode escape 1.1");
    } else { fail("Unicode escape 1.2"); }
    check("\{#0008}"=="\b", "Unicode escape 2");
    check("\{#0009}"=="\t", "Unicode escape 3");
    check("\{#000A}"=="\n", "Unicode escape 4");
    check("\{#000C}"=="\f", "Unicode escape 5");
    check("\{#000D}"=="\r", "Unicode escape 6");
    check("\{#005C}"=="\\", "Unicode escape 7");
    check("\{#0060}"=="\`", "Unicode escape 8");
    check("\{#0022}"=="\"", "Unicode escape 9");
    check("\{#0027}"=="\'", "Unicode escape 10");

    check(string ([]) == "", "string()");
    check(string {'h', 'i'}=="hi", "string(h,i)");
}

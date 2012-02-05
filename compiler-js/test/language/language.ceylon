variable Integer assertionCount:=0;
variable Integer failureCount:=0;

shared void assert(Boolean assertion, String message="") {
    assertionCount+=1;
    if (!assertion) {
        failureCount+=1;
        print("assertion failed \"" message "\"");
    }
}
shared void assertEqual(Equality actual, Equality expected, String message="") {
    assertionCount+=1;
    if (actual!=expected) {
        failureCount+=1;
        print("assertion failed \"" message "\": actual='" actual "', expected='" expected "'");
    }
}

shared void fail(String message) {
    assert(false, message);
}

shared void results() {
    print("assertions " assertionCount 
          ", failures " failureCount "");
}

void testCharacter() {
    Character c1 = `A`;
    //Character c2 = `𝄞`;
    //Character c3 = `Ũ`;
    assertEqual(c1.string, "A", "Character.string");
    //assertEqual(c2.string, "𝄞", "Character.string");
    //assertEqual(c3.string, "Ũ", "Character.string");
    assertEqual(`Ä`.lowercased, `ä`, "Character.lowercased");
    assertEqual(`x`.lowercased, `x`, "Character.lowercased");
    assertEqual(`ö`.uppercased, `Ö`, "Character.uppercased");
    assertEqual(`#`.uppercased, `#`, "Character.uppercased");
    assertEqual(`c`.titlecased, `C`, "Character.titlecased");
    assertEqual(`C`.titlecased, `C`, "Character.titlecased");
    assertEqual(`9`.titlecased, `9`, "Character.titlecased");
    assertEqual(`A`.whitespace, false, "Character.whitespace");
    assertEqual(` `.whitespace, true, "Character.whitespace");
    for (c in "\t") {
        assertEqual(c.whitespace, true, "Character.whitespace");
    }
    assertEqual(` `.control, false, "Character.control");
    for (c in "\r") {
        assertEqual(c.control, true, "Character.control");
    }
    assertEqual(`P`.uppercase, true, "Character.uppercase");
    assertEqual(`m`.uppercase, false, "Character.uppercase");
    assertEqual(`#`.uppercase, false, "Character.uppercase");
    assertEqual(`z`.lowercase, true, "Character.lowercase");
    assertEqual(`V`.lowercase, false, "Character.lowercase");
    assertEqual(`+`.lowercase, false, "Character.lowercase");
    assertEqual(`M`.titlecase, false, "Character.titlecase");
    assertEqual(`m`.titlecase, false, "Character.titlecase");
    assertEqual(`6`.titlecase, false, "Character.titlecase");
    
    assertEqual(`A`.successor, `B`, "Character.successor");
    assertEqual(`w`.predecessor, `v`, "Character.predecessor");
}

void testString() {
    assertEqual("".empty, true, "String.empty");
    assertEqual("x".empty, false, "String.empty");
    assertEqual("".size, 0, "String.size");
    String s1 = "abc";
    String s2 = "ä€Ũ\t";
    String s3 = "A𝄞`ŨÖ";
    assertEqual(s1.size, 3, "String.size");
    assertEqual(s2.size, 4, "String.size");
    assertEqual(s3.size, 5, "String.size");
    assertEqual((s1+s2).size, 7, "String.size");
    assertEqual((s1+s3).size, 8, "String.size");
    
    assertEqual("".shorterThan(0), false, "String.shorterThan");
    assertEqual("".shorterThan(1), true, "String.shorterThan");
    assertEqual("abc".shorterThan(3), false, "String.shorterThan");
    assertEqual("abc".shorterThan(4), true, "String.shorterThan");
    assertEqual("".longerThan(0), false, "String.longerThan");
    assertEqual("x".longerThan(0), true, "String.longerThan");
    assertEqual("abc".longerThan(3), false, "String.longerThan");
    assertEqual("abc".longerThan(2), true, "String.longerThan");
    
    variable Integer cnt := 0;
    variable String s4 := "";
    for (c in s3) {
        s4 := c.string + s4;
        ++cnt;
    }
    assertEqual(cnt, 5, "String.iterator");
    assertEqual(s4, "ÖŨ`𝄞A", "String.iterator");
    
    if (exists c = s1[-1]) {
        fail("String.item");
    }
    if (exists c = s1[0]) {
        assertEqual(c, `a`, "String.item");
    } else {
        fail("String.item");
    }
    if (exists c = s1[2]) {
        assertEqual(c, `c`, "String.item");
    } else {
        fail("String.item");
    }
    if (exists c = s1[3]) {
        fail("String.item");
    }
    if (exists c = s3[4]) {
        assertEqual(c, `Ö`, "String.item");
    } else {
        fail("String.item");
    }
    if (exists c = ""[0]) {
        fail("String.item");
    }
    
    assertEqual("".trimmed, "", "String.trimmed");
    assertEqual("x".trimmed, "x", "String.trimmed");
    assertEqual("  ".trimmed, "", "String.trimmed");
    assertEqual(" \tx \t".trimmed, "x", "String.trimmed");
    assertEqual(" \t𝄞\t".trimmed.size, 1, "String.trimmed.size");
    
    assertEqual("".initial(1), "", "String.initial");
    assertEqual("abc".initial(0), "", "String.initial");
    assertEqual("abc".initial(3), "abc", "String.initial");
    assertEqual("abc".initial(1), "a", "String.initial");
    assertEqual("𝄞abc".initial(2), "𝄞a", "String.intitial");
    assertEqual("𝄞abc".initial(2).size, 2, "String.intitial().size");
    assertEqual("".terminal(1), "", "String.terminal");
    assertEqual("abc".terminal(0), "", "String.terminal");
    assertEqual("abc".terminal(3), "abc", "String.terminal");
    assertEqual("abc".terminal(1), "c", "String.terminal");
    assertEqual("abc𝄞".terminal(2), "c𝄞", "String.terminal");
    assertEqual("abc𝄞".terminal(2).size, 2, "String.terminal().size");
    
    assertEqual("".startsWith("abc"), false, "String.startsWith");
    assertEqual("".startsWith(""), true, "String.startsWith");
    assertEqual("abc".startsWith(""), true, "String.startsWith");
    assertEqual("abc".startsWith("abc"), true, "String.startsWith");
    assertEqual("ab".startsWith("abc"), false, "String.startsWith");
    assertEqual("abc".startsWith("a"), true, "String.startsWith");
    assertEqual("abc".startsWith("b"), false, "String.startsWith");
    assertEqual("".endsWith("abc"), false, "String.endsWith");
    assertEqual("".endsWith(""), true, "String.endsWith");
    assertEqual("abc".endsWith(""), true, "String.endsWith");
    assertEqual("abc".endsWith("abc"), true, "String.endsWith");
    assertEqual("ab".endsWith("xab"), false, "String.endsWith");
    assertEqual("abc".endsWith("c"), true, "String.endsWith");
    assertEqual("abc".endsWith("b"), false, "String.endsWith");
}

shared void test() {
    testCharacter();
    testString();
    results();
}

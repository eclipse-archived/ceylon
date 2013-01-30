import check {...}

void testCharacter() {
    Character c1 = `A`;
    //Character c2 = `𝄞`;
    Character c3 = `Ũ`;
    checkEqual(c1.string, "A", "Character.string");
    //checkEqual(c2.string, "𝄞", "Character.string");
    checkEqual(c3.string, "Ũ", "Character.string");
    checkEqual(`Ä`.lowercased, `ä`, "Character.lowercased");
    checkEqual(`x`.lowercased, `x`, "Character.lowercased");
    checkEqual(`ö`.uppercased, `Ö`, "Character.uppercased");
    checkEqual(`#`.uppercased, `#`, "Character.uppercased");
    checkEqual(`c`.titlecased, `C`, "Character.titlecased");
    checkEqual(`C`.titlecased, `C`, "Character.titlecased");
    checkEqual(`9`.titlecased, `9`, "Character.titlecased");
    checkEqual(`A`.whitespace, false, "Character.whitespace");
    checkEqual(` `.whitespace, true, "Character.whitespace");
    for (c in "\t") {
        checkEqual(c.whitespace, true, "Character.whitespace");
    }
    checkEqual(` `.control, false, "Character.control");
    for (c in "\r") {
        checkEqual(c.control, true, "Character.control");
    }
    checkEqual(`P`.uppercase, true, "Character.uppercase");
    checkEqual(`m`.uppercase, false, "Character.uppercase");
    checkEqual(`#`.uppercase, false, "Character.uppercase");
    checkEqual(`z`.lowercase, true, "Character.lowercase");
    checkEqual(`V`.lowercase, false, "Character.lowercase");
    checkEqual(`+`.lowercase, false, "Character.lowercase");
    checkEqual(`M`.titlecase, false, "Character.titlecase");
    checkEqual(`m`.titlecase, false, "Character.titlecase");
    checkEqual(`6`.titlecase, false, "Character.titlecase");
    
    checkEqual(`A`.successor, `B`, "Character.successor");
    checkEqual(`w`.predecessor, `v`, "Character.predecessor");
}

void testString() {
    checkEqual("".empty, true, "String.empty");
    checkEqual("x".empty, false, "String.empty");
    checkEqual("".size, 0, "String.size");
    String s1 = "abc";
    String s2 = "ä€Ũ\t";
    String s3 = "A𝄞`ŨÖ";
    checkEqual(s1.size, 3, "String.size");
    checkEqual(s2.size, 4, "String.size");
    checkEqual(s3.size, 5, "String.size");
    checkEqual((s1+s2).size, 7, "String.size");
    checkEqual((s1+s3).size, 8, "String.size");
    
    checkEqual("".shorterThan(0), false, "String.shorterThan");
    checkEqual("".shorterThan(1), true, "String.shorterThan");
    checkEqual("abc".shorterThan(3), false, "String.shorterThan");
    checkEqual("abc".shorterThan(4), true, "String.shorterThan");
    checkEqual("".longerThan(0), false, "String.longerThan");
    checkEqual("x".longerThan(0), true, "String.longerThan");
    checkEqual("abc".longerThan(3), false, "String.longerThan");
    checkEqual("abc".longerThan(2), true, "String.longerThan");
    
    variable Integer cnt = 0;
    variable String s4 = "";
    for (c in s3) {
        s4 = c.string + s4;
        ++cnt;
    }
    checkEqual(cnt, 5, "String.iterator");
    checkEqual(s4, "ÖŨ`𝄞A", "String.iterator");

    Integer neg = -1;
    if (exists c = s1[neg]) {
        fail("String.item");
    }
    if (exists c = s1[0]) {
        checkEqual(c, `a`, "String.item");
    } else {
        fail("String.item");
    }
    if (exists c = s1[2]) {
        checkEqual(c, `c`, "String.item");
    } else {
        fail("String.item");
    }
    if (exists c = s1[3]) {
        fail("String.item");
    }
    if (exists c = s3[4]) {
        checkEqual(c, `Ö`, "String.item");
    } else {
        fail("String.item");
    }
    if (exists c = ""[0]) {
        fail("String.item");
    }
    
    checkEqual("".trimmed, "", "String.trimmed");
    checkEqual("x".trimmed, "x", "String.trimmed");
    checkEqual("  ".trimmed, "", "String.trimmed");
    checkEqual(" \tx \t".trimmed, "x", "String.trimmed");
    checkEqual(" \t𝄞\t".trimmed.size, 1, "String.trimmed.size");
    
    checkEqual("".initial(1), "", "String.initial");
    checkEqual("abc".initial(0), "", "String.initial");
    checkEqual("abc".initial(3), "abc", "String.initial");
    checkEqual("abc".initial(1), "a", "String.initial");
    checkEqual("𝄞abc".initial(2), "𝄞a", "String.intitial");
    checkEqual("𝄞abc".initial(2).size, 2, "String.intitial().size");
    checkEqual("".terminal(1), "", "String.terminal");
    checkEqual("abc".terminal(0), "", "String.terminal");
    checkEqual("abc".terminal(3), "abc", "String.terminal");
    checkEqual("abc".terminal(1), "c", "String.terminal");
    checkEqual("abc𝄞".terminal(2), "c𝄞", "String.terminal");
    checkEqual("abc𝄞".terminal(2).size, 2, "String.terminal().size");
    
    checkEqual("".startsWith("abc"), false, "String.startsWith");
    checkEqual("".startsWith(""), true, "String.startsWith");
    checkEqual("abc".startsWith(""), true, "String.startsWith");
    checkEqual("abc".startsWith("abc"), true, "String.startsWith");
    checkEqual("ab".startsWith("abc"), false, "String.startsWith");
    checkEqual("abc".startsWith("a"), true, "String.startsWith");
    checkEqual("abc".startsWith("b"), false, "String.startsWith");
    checkEqual("".endsWith("abc"), false, "String.endsWith");
    checkEqual("".endsWith(""), true, "String.endsWith");
    checkEqual("abc".endsWith(""), true, "String.endsWith");
    checkEqual("abc".endsWith("abc"), true, "String.endsWith");
    checkEqual("ab".endsWith("xab"), false, "String.endsWith");
    checkEqual("abc".endsWith("c"), true, "String.endsWith");
    checkEqual("abc".endsWith("b"), false, "String.endsWith");
    
    checkEqual("Hello
                 world!".normalized, "Hello world!", "String with line break");
}

shared void test() {
    testCharacter();
    testString();
    results();
}

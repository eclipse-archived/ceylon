void expect(Equality actual, Equality expected, String text) {
    print(text + ": actual='" + actual.string + "', expected='"
            + expected.string + "' => "
            + ((actual==expected) then "ok" else "FAIL"));
}
void succeed(String text) {
    print("[ok] " + text);
}
void fail(String text) {
    print("[NOT OK] " + text);
}

//Another test for the compiler.
void test_interpolate() {
    value s1 = "as it should";
    value interp = "String part " 1 " interpolation " 2 " works" s1 "";
    expect(interp, "String part 1 interpolation 2 worksas it should", "String Interpolation");
}

void testCharacter() {
    Character c1 = `A`;
    //Character c2 = `𝄞`;
    //Character c3 = `Ũ`;
    expect(c1.string, "A", "Character.string");
    //expect(c2.string, "𝄞", "Character.string");
    //expect(c3.string, "Ũ", "Character.string");
    //expect(`Ä`.lowercased, `ä`, "Character.lowercased");
    //expect(`x`.lowercased, `x`, "Character.lowercased");
    //expect(`ö`.uppercased, `Ö`, "Character.uppercased");
    //expect(`#`.uppercased, `#`, "Character.uppercased");
    expect(`A`.whitespace, false, "Character.whitespace");
    expect(` `.whitespace, true, "Character.whitespace");
    for (c in "\t") {
        expect(c.whitespace, true, "Character.whitespace");
    }
    expect(` `.control, false, "Character.control");
    for (c in "\r") {
        expect(c.control, true, "Character.control");
    }
    expect(`P`.uppercase, true, "Character.uppercase");
    expect(`m`.uppercase, false, "Character.uppercase");
    expect(`#`.uppercase, false, "Character.uppercase");
    expect(`z`.lowercase, true, "Character.lowercase");
    expect(`V`.lowercase, false, "Character.lowercase");
    expect(`+`.lowercase, false, "Character.lowercase");
    
    expect(`A`.successor, `B`, "Character.successor");
    expect(`w`.predecessor, `v`, "Character.predecessor");
}

void testString() {
    expect("".empty, true, "String.empty");
    expect("x".empty, false, "String.empty");
    expect("".size, 0, "String.size");
    String s1 = "abc";
    String s2 = "ä€Ũ\t";
    String s3 = "A𝄞`ŨÖ";
    expect(s1.size, 3, "String.size");
    expect(s2.size, 4, "String.size");
    expect(s3.size, 5, "String.size");
    expect((s1+s2).size, 7, "String.size");
    expect((s1+s3).size, 8, "String.size");
    
    expect("".shorterThan(0), false, "String.shorterThan");
    expect("".shorterThan(1), true, "String.shorterThan");
    expect("abc".shorterThan(3), false, "String.shorterThan");
    expect("abc".shorterThan(4), true, "String.shorterThan");
    expect("".longerThan(0), false, "String.longerThan");
    expect("x".longerThan(0), true, "String.longerThan");
    expect("abc".longerThan(3), false, "String.longerThan");
    expect("abc".longerThan(2), true, "String.longerThan");
    
    variable Integer cnt := 0;
    variable String s4 := "";
    for (c in s3) {
        s4 := c.string + s4;
        ++cnt;
    }
    expect(cnt, 5, "String.iterator");
    expect(s4, "ÖŨ`𝄞A", "String.iterator");
    
    if (exists c = s1[-1]) {
        fail("String.item");
    } else {
        succeed("String.item");
    }
    if (exists c = s1[0]) {
        expect(c, `a`, "String.item");
    } else {
        fail("String.item");
    }
    if (exists c = s1[2]) {
        expect(c, `c`, "String.item");
    } else {
        fail("String.item");
    }
    if (exists c = s1[3]) {
        fail("String.item");
    } else {
        succeed("String.item");
    }
    if (exists c = s3[4]) {
        expect(c, `Ö`, "String.item");
    } else {
        fail("String.item");
    }
    if (exists c = ""[0]) {
        fail("String.item");
    } else {
        succeed("String.item");
    }
    
    expect("".trimmed, "", "String.trimmed");
    expect("x".trimmed, "x", "String.trimmed");
    expect("  ".trimmed, "", "String.trimmed");
    expect(" \tx \t".trimmed, "x", "String.trimmed");
    expect(" \t𝄞\t".trimmed.size, 1, "String.trimmed.size");
    
    expect("".initial(1), "", "String.initial");
    expect("abc".initial(0), "", "String.initial");
    expect("abc".initial(3), "abc", "String.initial");
    expect("abc".initial(1), "a", "String.initial");
    expect("𝄞abc".initial(2), "𝄞a", "String.intitial");
    expect("𝄞abc".initial(2).size, 2, "String.intitial().size");
    expect("".terminal(1), "", "String.terminal");
    expect("abc".terminal(0), "", "String.terminal");
    expect("abc".terminal(3), "abc", "String.terminal");
    expect("abc".terminal(1), "c", "String.terminal");
    expect("abc𝄞".terminal(2), "c𝄞", "String.terminal");
    expect("abc𝄞".terminal(2).size, 2, "String.terminal().size");
    
    expect("".startsWith("abc"), false, "String.startsWith");
    expect("".startsWith(""), true, "String.startsWith");
    expect("abc".startsWith(""), true, "String.startsWith");
    expect("abc".startsWith("abc"), true, "String.startsWith");
    expect("ab".startsWith("abc"), false, "String.startsWith");
    expect("abc".startsWith("a"), true, "String.startsWith");
    expect("abc".startsWith("b"), false, "String.startsWith");
    expect("".endsWith("abc"), false, "String.endsWith");
    expect("".endsWith(""), true, "String.endsWith");
    expect("abc".endsWith(""), true, "String.endsWith");
    expect("abc".endsWith("abc"), true, "String.endsWith");
    expect("ab".endsWith("xab"), false, "String.endsWith");
    expect("abc".endsWith("c"), true, "String.endsWith");
    expect("abc".endsWith("b"), false, "String.endsWith");
}

void test_stringbuilder() {
    value sb = StringBuilder();
    expect(sb.string, "", "StringBuilder");
    sb.append("H");
    expect(sb.string, "H", "StringBuilder");
    sb.appendCharacter(`E`);
    expect(sb.string, "HE", "StringBuilder");
    sb.appendAll("LL", "O", "!!!");
    expect(sb.string, "HELLO!!!", "StringBuilder");
    sb.appendSpace();
    expect(sb.string, "HELLO!!! ", "StringBuilder");
}

shared void test() {
    print("--- Start Language Module Tests ---");
    test_largest();
    test_smallest();
    test_max();
    test_min();
    test_join();
    test_zip();
    test_coalesce();
    test_append();
    test_entries();
    test_exists_nonempty();
    test_foreach();
    test_iterators();
    test_ranges();
    test_interpolate();
    test_stringbuilder();
    testCharacter();
    //testString();
    print("--- End Language Module Tests ---");
}

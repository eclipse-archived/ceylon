void expect(Equality actual, Equality expected, String text) {
    if (actual == expected) {
        print("[ok] " + text + ": '" + actual.string + "'");
    } else {
        print("[NOT OK] " + text + ": actual='" + actual.string + "', expected='"
              + expected.string);
    }
}

void testIntegerOperators() {
    
    variable Integer i1 := -4;
    i1 := -i1;
    expect(i1, 4, "negation");
    i1 := + (-987654);
    expect(i1, -987654, "positive");
    i1 := +0;
    expect(i1, 0, "+0=0");
    i1 := -0;
    expect(i1, 0, "+0=0");
        
    variable Integer i2 := 123 + 456;
    expect(i2, 579, "addition");
    i1 := i2 - 16;
    expect(i1, 563, "subtraction");
    i2 := -i1 + i2 - 1;
    expect(i2, 15, "-i1+i2-1");
        
    i1 := 3 * 7;
    expect(i1, 21, "multiplication");
    i2 := i1 * 2;
    expect(i2, 42, "multiplication");
    i2 := 17 / 4;
    expect(i2, 4, "integer division");
    i1 := i2 * 516 / -i1;
    expect(i1, -98, "i2*516/-i1");
    
    i1 := 15 % 4;
    expect(i1, 3, "modulo");
    i2 := 312 % 12;
    expect(i2, 0, "modulo");

    i1 := 2 ** 10;
    expect(i1, 1024, "power");
    i2 := 100 ** 6;
    expect(i2, 1000000000000, "power");
}

void testFloatOperators() {
    
    variable Float f1 := -4.2;
    f1 := -f1;
    expect(f1, 4.2, "negation");
    f1 := + (-987654.9925567);
    expect(f1, -987654.9925567, "positive");
    f1 := +0.0;
    expect(f1, 0.0, "+0.0=0.0");
    f1 := -0.0;
    expect(f1, 0.0, "-0.0=0.0");
        
    variable Float f2 := 3.14159265 + 456.0;
    expect(f2, 459.14159265, "addition");
    f1 := f2 - 0.0016;
    expect(f1, 459.13999265, "subtraction");
    f2 := -f1 + f2 - 1.2;
    expect(f2, -1.1984000000000037, "-f1+f2-1.2");
    
    f1 := 3.0 * 0.79;
    expect(f1, 2.37, "multiplication");
    f2 := f1 * 2.0e13;
    expect(f2, 47400000000000.0, "multiplication");
    f2 := 17.1 / 4.0E-18;
    expect(f2, 4275000000000000000.0, "division");
    f1 := f2 * 51.6e2 / -f1;
    expect(f2, 4275000000000000000.0, "f2*51.6e2/-f1");
        
    f1 := 150.0 ** 0.5;
    expect(f1, 12.24744871391589, "power");
}

class OpTest1() {}

void testBooleanOperators() {
    value o1 = OpTest1();
    value o2 = OpTest1();
    variable Boolean b1 := o1 === o2;
    expect(b1, false, "identity");
    variable Boolean b2 := o1 === o1;
    expect(b2, true, "identity");

    b1 := o1 == o2;
    expect(b1, false, "equals");
    b2 := o1 == o1;    
    expect(b2, true, "equals");
    b1 := 1 == 2;
    expect(b1, false, "equals");
    b2 := 1 != 2;
    expect(b2, true, "not equal");
    variable Boolean b3 := !b2;
    expect(b3, false, "not");
        
    b1 := true && false;
    expect(b1, false, "and");
    b2 := b1 && true;
    expect(b2, false, "and");
    b3 := true && true;
    expect(b3, true, "and");
    b1 := true || false;
    expect(b1, true, "or");
    b2 := false || b1;
    expect(b2, true, "or");
    b3 := false || false;
    expect(b3, false, "or");
}

void testComparisonOperators() {
    Comparison c1 = "str1" <=> "str2";
    expect(c1, smaller, "compare");
    Comparison c2 = "str2" <=> "str1";
    expect(c2, larger, "compare");
    Comparison c3 = "str1" <=> "str1";
    expect(c3, equal, "compare");
    Comparison c4 = "" <=> "";
    expect(c4, equal, "compare");
    Comparison c5 = "str1" <=> "";
    expect(c5, larger, "compare");
    Comparison c6 = "" <=> "str2";
    expect(c6, smaller, "compare");
    
    variable Boolean b1 := "str1" < "str2";
    expect(b1, true, "smaller");
    variable Boolean b2 := "str1" > "str2";
    expect(b2, false, "larger");
    variable Boolean b3 := "str1" <= "str2";
    expect(b3, true, "small as");
    variable Boolean b4 := "str1" >= "str2";
    expect(b4, false, "large as");
    b1 := "str1" < "str1";
    expect(b1, false, "smaller");
    b2 := "str1" > "str1";
    expect(b2, false, "larger");
    b3 := "str1" <= "str1";
    expect(b3, true, "small as");
    b4 := "str1" >= "str1";
    expect(b4, true, "large as");
}

void testOtherOperators() {
    Integer->String entry = 47->"hi there";
    expect(entry.key, 47, "entry key");
    expect(entry.item, "hi there", "entry item");
    value entry2 = true->entry;
    expect(entry2.key, true, "entry key");
    expect(entry2.item, 47->"hi there", "entry item");
            
    String s1 = true then "ok" else "noo";
    expect(s1, "ok", "then/else");
    String s2 = false then "what?" else "great"; 
    expect(s2, "great", "then/else");
}

void testCollectionOperators() {
    value seq1 = { "one", "two" };
    String s1 = seq1[0]?"null";
    expect(s1, "one", "lookup");
    String s2 = seq1[2]?"null";
    expect(s2, "null", "lookup");
    String s3 = seq1[-1]?"null"; 
    expect(s3, "null", "lookup");           
}

void testNullsafeOperators() {
    String[] seq = { "hi" };
    String s1 = seq[0]?"null";
    expect(s1, "hi", "default");
    String s2 = seq[1]?"null";
    expect(s2, "null", "default");
}

void testIncDecOperators() {
    variable Integer i1 := 1;
    void f1() {
        Integer i2 = ++i1;
        expect(i1, 2, "prefix increment");
        expect(i2, 2, "prefix increment");
    }
    f1();
    
    class C1() { shared variable Integer i := 1; }
    C1 c1 = C1();
    variable Integer i3 := 0;
    C1 f2() {
        ++i3;
        return c1;
    }
    Integer i4 = ++f2().i;
    expect(i4, 2, "prefix increment");
    expect(c1.i, 2, "prefix increment");
    expect(i3, 1, "prefix increment");
    
    void f3() {
        Integer i2 = --i1;
        expect(i1, 1, "prefix decrement");
        expect(i2, 1, "prefix decrement");
    }
    f3();
    
    Integer i5 = --f2().i;
    expect(i5, 1, "prefix decrement");
    expect(c1.i, 1, "prefix decrement");
    expect(i3, 2, "prefix decrement");
    
    void f4() {
        Integer i2 = i1++;
        expect(i1, 2, "postfix increment");
        expect(i2, 1, "postfix increment");
    }
    f4();
    
    Integer i6 = f2().i++;
    expect(i6, 1, "postfix increment");
    expect(c1.i, 2, "postfix increment");
    expect(i3, 3, "postfix increment");
    
    void f5() {
        Integer i2 = i1--;
        expect(i1, 1, "postfix decrement");
        expect(i2, 2, "postfix decrement");
    }
    f5();
    
    Integer i7 = f2().i--;
    expect(i7, 2, "postfix decrement");
    expect(c1.i, 1, "postfix decrement");
    expect(i3, 4, "postfix decrement");
}

shared void test() {
    print("--- Start Operator Tests ---");
    testIntegerOperators();
    testFloatOperators();
    testBooleanOperators();
    testComparisonOperators();
    testOtherOperators();
    testCollectionOperators();
    testNullsafeOperators();
    testIncDecOperators();
    print("--- End Operator Tests ---");
}
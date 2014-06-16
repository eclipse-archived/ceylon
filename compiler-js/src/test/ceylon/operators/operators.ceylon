import check {...}

void testIntegerOperators() {
    
    variable Integer i1 = -4;
    i1 = -i1;
    check(i1==4, "negation");
    i1 = + (-987654);
    check(i1==-987654, "positive");
    i1 = +0;
    check(i1==0, "+0=0");
    i1 = -0;
    check(i1==0, "+0=0");
        
    variable Integer i2 = 123 + 456;
    check(i2==579, "addition");
    i1 = i2 - 16;
    check(i1==563, "subtraction");
    i2 = -i1 + i2 - 1;
    check(i2==15, "-i1+i2-1");
        
    i1 = 3 * 7;
    check(i1==21, "multiplication");
    i2 = i1 * 2;
    check(i2==42, "multiplication");
    i2 = 17 / 4;
    check(i2==4, "integer division");
    i1 = i2 * 516 / -i1;
    check(i1==-98, "i2*516/-i1");
    
    i1 = 15 % 4;
    check(i1==3, "modulo");
    i2 = 312 % 12;
    check(i2==0, "modulo");

    i1 = 2 ^ 10;
    check(i1==1024, "power");
    i2 = 10 ^ 6;
    check(i2==1000000, "power");
}

void testFloatOperators() {
    
    variable Float f1 = -4.2;
    f1 = -f1;
    check(f1==4.2, "negation");
    f1 = + (-987654.9925567);
    check(f1==-987654.9925567, "positive");
    f1 = +0.0;
    check(f1==0.0, "+0.0=0.0");
    f1 = -0.0;
    check(f1==0.0, "-0.0=0.0");
        
    variable Float f2 = 3.14159265 + 456.0;
    check(f2==459.14159265, "addition");
    f1 = f2 - 0.0016;
    check(f1==459.13999265, "subtraction");
    f2 = -f1 + f2 - 1.2;
    check(f2==-1.1984000000000037, "-f1+f2-1.2");
    
    f1 = 3.0 * 0.79;
    check(f1==2.37, "multiplication");
    f2 = f1 * 2.0e13;
    check(f2==47400000000000.0, "multiplication");
    f2 = 17.1 / 4.0E-18;
    check(f2==4275000000000000000.0, "division");
    f1 = f2 * 51.6e2 / -f1;
    check(f2==4275000000000000000.0, "f2*51.6e2/-f1");
        
    f1 = 150.0 ^ 0.5;
    check(f1==12.24744871391589, "power");
}

class OpTest1() {}

void testBooleanOperators() {
    value o1 = OpTest1();
    value o2 = OpTest1();
    variable Boolean b1 = o1 === o2;
    check(!b1, "identity");
    variable Boolean b2 = o1 === o1;
    check(b2, "identity");

    b1 = o1 == o2;
    check(!b1, "equals");
    b2 = o1 == o1;    
    check(b2, "equals");
    b1 = 1 == 2;
    check(!b1, "equals");
    b2 = 1 != 2;
    check(b2, "not equal");
    variable Boolean b3 = !b2;
    check(!b3, "not");
        
    b1 = true && false;
    check(!b1, "and");
    b2 = b1 && true;
    check(!b2, "and");
    b3 = true && true;
    check(b3, "and");
    b1 = true || false;
    check(b1, "or");
    b2 = false || b1;
    check(b2, "or");
    b3 = false || false;
    check(!b3, "or");
    b1=true;
    check(b1&&=true, "&&=");
    check(b1||=false, "||=");
}

void testComparisonOperators() {
    Comparison c1 = "str1" <=> "str2";
    check(c1==smaller, "compare");
    Comparison c2 = "str2" <=> "str1";
    check(c2==larger, "compare");
    Comparison c3 = "str1" <=> "str1";
    check(c3==equal, "compare");
    Comparison c4 = "" <=> "";
    check(c4==equal, "compare");
    Comparison c5 = "str1" <=> "";
    check(c5==larger, "compare");
    Comparison c6 = "" <=> "str2";
    check(c6==smaller, "compare");
    
    variable Boolean b1 = "str1" < "str2";
    check(b1, "smaller");
    variable Boolean b2 = "str1" > "str2";
    check(!b2, "larger");
    variable Boolean b3 = "str1" <= "str2";
    check(b3, "small as");
    variable Boolean b4 = "str1" >= "str2";
    check(!b4, "large as");
    b1 = "str1" < "str1";
    check(!b1, "smaller");
    b2 = "str1" > "str1";
    check(!b2, "larger");
    b3 = "str1" <= "str1";
    check(b3, "small as");
    b4 = "str1" >= "str1";
    check(b4, "large as");
    //within
    value a = 0;
    value c = 10;
    check(a < 5 < c, "``a``<5<``c``");
    check(a <= 0 < c, "``a``<=0<``c``");
    check(a < 10 <= c, "``a``<10<=``c``");
    check(a <= 0 <= c, "``a``<=0<=``c``");
    check(a <= 10 <= c, "``a``<=10<=``c``");
    check(!(a < 15 < c), "``a``<15<``c`` WTF");
    check(!(a <= 10 < c), "``a``<=10<``c`` WTF");
    check(!(a < 0 <= c), "``a``<0<=``c`` WTF");
    check(!(a <= 11 <= c), "``a``<=11<=``c`` WTF");
    check(!(a <= -1 <= c), "``a``<=-1<=``c`` WTF");
}

void testOtherOperators() {
    Integer->String entry = 47->"hi there";
    check(entry.key==47, "entry key");
    check(entry.item=="hi there", "entry item");
    value entry2 = true->entry;
    check(entry2.key==true, "entry key");
    check(entry2.item==47->"hi there", "entry item");
            
    String s1 = true then "ok" else "noo";
    check(s1=="ok", "then/else 1");
    String s2 = false then "what?" else "great"; 
    check(s2=="great", "then/else 2");
}

void testCollectionOperators() {
    value seq1 = [ "one", "two" ];
    String s1 = seq1[0]; //not optional anymore!
    check(s1=="one", "lookup 1");
    String|Null s2 = seq1[2]; //not optional anymore!
    check(!s2 exists, "lookup 2");
    String|Null s3 = seq1[-1]; //not optional anymore!
    check(!s3 exists, "lookup 3");
    //variable Sequence<String>? unsafe = seq1;
    //check(unsafe?[0] exists, "safe index");
    //unsafe = null;
    //check(!unsafe?[0] exists, "safe index");
}

class NullsafeTest() {
    shared Integer f() {return 1;}
    shared Integer? f2(Integer? x()) {
        return x();
    }
}

Integer? nullsafeTest(Integer? f()) {
    return f();
}

void testNullsafeOperators() {
    String[] seq = [ "hi" ];
    String s1 = seq[0] else "nuLl";
    check(s1=="hi", "default 1");
    String s2 = seq[1] else "nuLl";
    check(s2=="nuLl", "default 2");
    
    String? s3 = null;
    String? s4 = "test";
    String s5 = s3?.uppercased else "nuLl";
    String s6 = s4?.uppercased else "null";
    check(s5=="nuLl", "nullsafe member 1");
    check(s6=="TEST", "nullsafe member 2");
    NullsafeTest? obj = null;
    Integer? i = obj?.f();
    check(!i exists, "nullsafe invoke");
    Callable<Integer?,[]> f2 = obj?.f;
    check(!nullsafeTest(f2) exists, "nullsafe method ref");
    Callable<Integer?,[]>? f3 = obj?.f;
    check(f3 exists, "nullsafe method ref 2");
    obj?.f();
    check(!obj?.f() exists, "nullsafe simple call");
    NullsafeTest? getNullsafe() { return obj; }
    function f4() => getNullsafe()?.f();
    Integer? result_f4 = f4();
    check(!result_f4 exists, "nullsafe invoke 2");
    Integer? i2 = getNullsafe()?.f();
    check(!i2 exists, "nullsafe invoke 3");
    check(!NullsafeTest().f2(getNullsafe()?.f) exists, "nullsafe method ref 3");
    NullsafeTest? obj2 = NullsafeTest();
    if (exists i3 = obj2?.f()) {
        check(i3==1, "nullsafe invoke 4 (result)");
    } else {
        fail("nullsafe invoke 4 (null)");
    }
    Integer? obj2_f() => obj2?.f();
    if (exists i3 = obj2_f()) {
        check(i3==1, "nullsafe method ref 4 (result)");
    } else {
        fail("nullsafe method ref 4 (null)");
    }
}

void testIncDecOperators() {
    variable Integer x0 = 1;
    Integer x { return x0; } assign x { x0 = x; }
    
    variable Integer i1 = 1;
    void f1() {
        Integer i2 = ++i1;
        Integer x2 = ++x;
        check(i1==2, "prefix increment 1");
        check(i2==2, "prefix increment 2");
        check(x==2, "prefix increment 3");
        check(x2==2, "prefix increment 4");
    }
    f1();
    
    class C1() {
        shared variable Integer i = 1;
        variable Integer x0 = 1;
        shared Integer x { return x0; } assign x { x0 = x; }
    }
    C1 c1 = C1();
    variable Integer i3 = 0;
    C1 f2() {
        ++i3;
        return c1;
    }
    Integer i4 = ++f2().i;
    Integer x4 = ++f2().x;
    check(i4==2, "prefix increment 5");
    check(c1.i==2, "prefix increment 6");
    check(x4==2, "prefix increment 7");
    check(c1.x==2, "prefix increment 8");
    check(i3==2, "prefix increment 9");
    
    void f3() {
        Integer i2 = --i1;
        check(i1==1, "prefix decrement");
        check(i2==1, "prefix decrement");
    }
    f3();
    
    Integer i5 = --f2().i;
    check(i5==1, "prefix decrement");
    check(c1.i==1, "prefix decrement");
    check(i3==3, "prefix decrement");
    
    void f4() {
        Integer i2 = i1++;
        Integer x2 = x++;
        check(i1==2, "postfix increment 1");
        check(i2==1, "postfix increment 2");
        check(x==3, "postfix increment 3");
        check(x2==2, "postfix increment 4");
    }
    f4();
    
    Integer i6 = f2().i++;
    Integer x6 = f2().x++;
    check(i6==1, "postfix increment 5");
    check(c1.i==2, "postfix increment 6");
    check(x6==2, "postfix increment 7 ");
    check(c1.x==3, "postfix increment 8 ");
    check(i3==5, "postfix increment 9");
    
    void f5() {
        Integer i2 = i1--;
        check(i1==1, "postfix decrement");
        check(i2==2, "postfix decrement");
    }
    f5();
    
    Integer i7 = f2().i--;
    check(i7==2, "postfix decrement");
    check(c1.i==1, "postfix decrement");
    check(i3==6, "postfix decrement");
}

void testArithmeticAssignOperators() {
    variable Integer i1 = 1;
    variable Integer x0 = 1;
    Integer x { return x0; } assign x { x0=x; } 
    i1 += 10;
    x += 10;
    check(i1==11, "+= operator 1");
    check(x==11, "+= operator 2");
    
    variable Integer i2 = (i1 += -5);
    variable Integer x2 = (x += -5);
    check(i2==6, "+= operator 3");
    check(i1==6, "+= operator 4");
    check(x2==6, "+= operator 5");
    check(x==6, "+= operator 6");
    
    class C1() {
        shared variable Integer i = 1;
        variable Integer x0 = 1;
        shared Integer x { return x0; } assign x { x0=x; }
    }
    C1 c1 = C1();
    variable Integer i3 = 0;
    C1 f() {
        ++i3;
        return c1;
    }
    
    i2 = (f().i += 11);
    x2 = (f().x += 11);
    check(i2==12, "+= operator 7");
    check(c1.i==12, "+= operator 8");
    check(x2==12, "+= operator 9");
    check(c1.x==12, "+= operator 10");
    check(i3==2, "+= operator 11");
    
    i2 = (i1 -= 14);
    check(i1==-8, "-= operator");
    check(i2==-8, "-= operator");
    
    i2 = (i1 *= -3);
    check(i1==24, "*= operator");
    check(i2==24, "*= operator");
    
    i2 = (i1 /= 5);
    check(i1==4, "/= operator");
    check(i2==4, "/= operator");
    
    i2 = (i1 %= 3);
    check(i1==1, "%= operator");
    check(i2==1, "%= operator");
}

void testAssignmentOperator() {
    variable Integer i1 = 1;
    variable Integer i2 = 2;
    variable Integer i3 = 3;
    check((i1=i2=i3)==3, "assignment 1");
    check(i1==3, "assignment 2");
    check(i2==3, "assignment 3");
    
    Integer x1 { return i1; } assign x1 { i1 = x1; }
    Integer x2 { return i2; } assign x2 { i2 = x2; }
    Integer x3 { return i3; } assign x3 { i3 = x3; }
    i1 = 1;
    i2 = 2;
    check((x1=x2=x3)==3, "assignment 4");
    check(x1==3, "assignment 5");
    check(x2==3, "assignment 6");
    
    class C() {
        shared variable Integer i = 1;
        variable Integer x0 = 1;
        shared Integer x { return x0; } assign x { x0=x; }
    }
    C o1 = C();
    C o2 = C();
    check((o1.i=o2.i=3)==3, "assignment 7");
    check(o1.i==3, "assignment 8");
    check(o2.i==3, "assignment 9");
    check((o1.x=o2.x=3)==3, "assignment 10");
    check(o1.x==3, "assignment 11");
    check(o2.x==3, "assignment 12");
}

void testRangeOps() {
    value seq = [ "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten" ];
    check(seq[1..2] == ["two", "three" ], "seq[1:2] ``seq[1..2]``");
    check(seq[3..5] == ["four", "five", "six"], "seq[3..5] ``seq[3..5]``");
    check("test"[1..2] == "es", "test[1..2] ``("test"[1..2])``");
    check("hello"[2..2] == "l", "hello[2..2] ``("hello"[2..2])``");
    value s2 = 0..3;
    value s3 = 2..5;
    check(s2.size == 4, "0..3 [1]");
    check(s2[0] == 0, "0..3 [2.1]");
    check(s2.first == 0, "0..3 [2.2]");
    if (exists x=s2[2]) {
        check(x == 2, "0..3 [3]");
    } else { fail("0..3 [3]"); }
    check(s3.size == 4, "2..5 [1]");
    check(s3[0] == 2, "2..5 [1]");
    if (exists x=s3[2]) {
        check(x == 4, "2..5 [2]");
    } else { fail("2..5 [2]"); }
    if (exists x=s3[3]) {
        check(x == 5, "2..5 [3]");
    } else { fail("2..5 [3]"); }
    if (exists x=s3[4]) {
        fail("2..5 [3]");
    }
    check((1..-1).decreasing, "1..-1 decreasing");
    check((1..-1).size==3, "1..-1 size");
    variable value rounds=0;
    for (i in 0..10) { rounds++; }
    check(rounds==11, "for(range) 1");
    rounds=0;
    variable value testChar='.';
    for (i in 'a'..'e') {
        rounds++;
        testChar=i;
    }
    check(rounds==5, "for(range) 2");
    check(testChar=='e', "for(range) 2");
    rounds=-1;
    variable value testInt=0;
    for (i in 1..rounds) {
        testInt++;
    }
    check(testInt==3, "for(range) 3");
    rounds=0;
    for (i in testInt..5) {
        rounds++;
    }
    check(rounds==3, "for(range) 4");
    rounds=0;
    for (i in testInt..0) {
        rounds++;
    }
    check(rounds==4, "for(range) 5");
}

void testSegments() {
    value seq = [ "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten" ];
    check(seq[1:2] == ["two", "three" ], "seq[1:2] ``seq[1:2]``");
    check(seq[3:5] == ["four", "five", "six", "seven", "eight"], "seq[3:5] ``seq[3:5]``");
    check("test"[1:2] == "es", "test[1:2] ``("test"[1:2])``");
    check("hello"[2:2] == "ll", "hello[2:2] ``("hello"[2:2])``");
    check(seq*.uppercased == [ "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN"], "spread op");
    value s2 = 0:3;
    value s3 = 2:5;
    check(s2.size == 3, "0:3 [1]");
    if (exists x=s2[0]) {
        check(x == 0, "0:3 [2]");
    } else { fail("0:3 [2]"); }
    if (exists x=s2[2]) {
        check(x == 2, "0:3 [3]");
    } else { fail("0:3 [3]"); }
    check(s3.size == 5, "2:5 [1]");
    if (exists x=s3[0]) {
        check(x == 2, "2:5 [1]");
    } else { fail("2:5 [1]"); }
    if (exists x=s3[2]) {
        check(x == 4, "2:5 [2]");
    } else { fail("2:5 [2]"); }
    if (exists x=s3[4]) {
        check(x == 6, "2:5 [3]");
    } else { fail("2:5 [3]"); }
    check(!1:0 nonempty, "1:0 empty");
    check(!1:-1 nonempty, "1:-1 empty");
    variable value rounds=0;
    for (i in 0:10) { rounds++; }
    check(rounds==10, "for(segment) 1");
    rounds=0;
    variable value testChar='.';
    for (i in 'a':5) {
        rounds++;
        testChar=i;
    }
    check(rounds==5, "for(segment) 2");
    check(testChar=='e', "for(segment) 2");
    rounds=-1;
    for (i in 'a':rounds) {
        testChar=i;
    }
    check(testChar=='e', "for(segment) 3");
    rounds=2;
    for (i in 'a':rounds) {
        testChar=i;
    }
    check(testChar=='b', "for(segment) 4");
}

void compareStringNumber() {
    //This is pretty fucking basic and I just found out it's broken in js
    Object n1 = 1;
    Object s1 = "1";
    Object n2 = 1.0;
    Object s2 = "1.0";
    check(n1 != s1, "Integer and String should NOT be equal!");
    check(s1 != n1, "String and Integer should NOT be equal!");
    check(n2 != s2, "Float and String sould NOT be equal");
    check(s2 != n2, "String and Float should NOT be equal");
}

class TestSet<out Element>({Element*} e) satisfies Set<Element>
    given Element satisfies Object {
    hash = e.hash;
    shared actual TestSet<Element> clone() => this;
    shared actual Iterator<Element> iterator() => e.iterator();
    shared actual Set<Element|Other> union<Other>(Set<Other> other)
            given Other satisfies Object {
        value others = [for (o in e) if (is Other o, !o in other) o];
        return TestSet(other.chain(others));
    }
    shared actual Set<Element&Other> intersection<Other>(Set<Other> set)
            given Other satisfies Object {
        return TestSet{for (elem in e) if (is Other elem, elem in set) elem };
    }
    shared actual Set<Element|Other> exclusiveUnion<Other>(Set<Other> set)
            given Other satisfies Object {
        return set;
    }
    shared actual Set<Element> complement<Other>(Set<Other> set)
            given Other satisfies Object {
        return TestSet{for (o in e) if (!o in set) o};
    }
    shared actual Boolean equals(Object other) {
        if (is TestSet<Element> other) {
            return size==other.size && every((Object o) => o in other) && other.every((Object o) => o in this);
        }
        return false;
    }
}

void testSetOperators() {
  variable value s1 = TestSet{1,2,3,4};
  value s2 = TestSet{4,5,6};
  check((s1|s2).size == 6, "|");
  check((s1~s2).size == 3, "~");
  check((s1&s2).size == 1, "&");
  check((s1|=s2).size == 6, "|=");
  check((s1&=s2) == s2, "&=");
  check((s1~=s2).size == 0, "~=");
}

void testIssue315() {
  String? s=null;
  check((s else "1") == "1", "Issue 315 [1]");
  dynamic {
    dynamic z=value{a=1;};
    check((z.b else "2") == "2", "Issue 315 [2]");
  }
}

shared void test() {
    testIntegerOperators();
    testFloatOperators();
    testBooleanOperators();
    testComparisonOperators();
    testOtherOperators();
    testCollectionOperators();
    testNullsafeOperators();
    testIncDecOperators();
    testArithmeticAssignOperators();
    testAssignmentOperator();
    testSetOperators();
    testRangeOps();
    testSegments();
    testEnumerations();
    compareStringNumber();
    testIssue315();
    results();
}

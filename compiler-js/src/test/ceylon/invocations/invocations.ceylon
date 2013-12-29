import functions { ... }
import check { check, results }

String mixseqs({Character*} chars, Integer* nums) => "C:``(chars.first else "?")`` #``(nums[0] else "?")``";
String({String*})(String) staticJoinTest = String.join;

shared void test() {
    helloWorld();
    helloWorld{};
    hello("world");
    hello { name="world"; };
    helloAll("someone", "someone else");
    helloAll { names=["someone","someone else"];};
    String s1 = toString(99);
    String s2 = toString { obj=99; };    
    Float f1 = add(1.0, -1.0);
    Float f2 = add { x=1.0; y=-1.0; };
    void p(Integer i) {
        print(i);
    }
    repeat(10,p);
    testNamedArguments();
    testQualified();
    check(mixseqs(['a','b'],1,2,3)=="C:a #1");
    check(mixseqs({'b','c'},2,3,4)=="C:b #2");
    check(mixseqs([*"hola"],3,4,5)=="C:h #3");
    check(mixseqs{for (c in "hola") c.uppercased}=="C:H #?");
    check(mixseqs{nums=[2,1];*"hola"}=="C:h #2");
    check(mixseqs{nums=[4]; for (c in "hola") c}=="C:h #4");
    check(mixseqs{*"hola"}=="C:h #?");
    check(mixseqs{'H','I'}=="C:H #?");
    check(staticJoinTest("**")({"a","b","c"})=="a**b**c", "static String.join test");
    testSpread();
    testIssues();
    results();
}

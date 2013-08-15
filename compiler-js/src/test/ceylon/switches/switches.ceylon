import check {...}

abstract class SwitchTest1() of testcase1|testcase2|testcase3 {}
object testcase1 extends SwitchTest1(){}
object testcase2 extends SwitchTest1(){}
object testcase3 extends SwitchTest1(){}

String testMatch1(SwitchTest1 st) {
    switch (st)
    case (testcase1) { return "case1"; }
    case (testcase2) { return "case2"; }
    else { return "case3"; }
}
String testMatch2(SwitchTest1 st) {
    switch(st)
    case(testcase1, testcase2) { return "case 1|2"; }
    case(testcase3) { return "case 3"; }
}

Integer|Float testIs1(Integer|Float x) {
    switch (x)
    case (is Integer) { return x+1; }
    case (is Float) { return x+1.0; }
    else { return infinity; }
}

class TestIs2(Integer|String x) {
    Integer|String y = x;
    variable shared String z;
    switch (y)
    case (is Integer) { z = (y+1).string; }
    case (is String) { z = y.uppercased; }
    else { z = ""; }
    shared String f() {
        switch (y)
        case (is Integer) { return (y+1).string; }
        case (is String) { return y.uppercased; }
        else { return ""; }
    }
}

shared void test() {
    value enums = [1, 2.0];
    //is cases
    value e0 = enums[0];
    switch(e0)
    case(is Integer) {}
    else { fail("FLOAT? WTF?"); }

    value old_e1 = enums[1]; //This is now a Float
    Integer|Float e1 = enums[1];
    switch(e1)
    case(is Integer) { fail("INTEGER? WTF?"); }
    case(is Float) {}
    else { fail("Null!!! WTF?"); }

    //is+continue
    for (e in enums) {
        String result;
        switch (e)
        case (is Integer) {
            result="int";
        }
        case (is Float) {
            continue;
        }
        check(result=="int", "is/continue");
    }

    //matches
    check(testMatch1(testcase1)=="case1", "match1");
    check(testMatch1(testcase2)=="case2", "match 2");
    check(testMatch1(testcase3)=="case3", "match 3");
    check(testMatch2(testcase1)=="case 1|2", "match 4");
    check(testMatch2(testcase2)=="case 1|2", "match 5");
    check(testMatch2(testcase3)=="case 3", "match 6");

    //is
    check(testIs1(1) == 2, "is1");
    check(testIs1(1.5) == 2.5, "is2");
    check(TestIs2(1).z == "2", "is3");
    check(TestIs2("ab").z == "AB", "is3");
    check(TestIs2(1).f() == "2", "is3");
    check(TestIs2("ab").f() == "AB", "is3");

    //TODO satisfies

    Object lits1=1;
    switch(lits1)
    case(5) { fail("WTF? it's 1"); }
    case("1") { fail("it's not even a String!"); }
    case('1') { fail("not a char wither"); }
    case(1) { check(true, "case with literals"); }
    else { fail("it WAS one"); }
    Object? lits2 = null;
    switch(lits2)
    case(1) { fail("it's null"); }
    case("2") { fail("it's null!"); }
    case(null) { check(true, "case with literals against optional"); }
    else { fail("how did you get here?"); }

    results();
}


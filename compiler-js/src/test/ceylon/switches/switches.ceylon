variable Integer assertionCount:=0;
variable Integer failureCount:=0;

shared void assert(Boolean assertion, String message="") {
    assertionCount+=1;
    if (!assertion) {
        failureCount+=1;
        print("assertion failed \"" message "\"");
    }
}

shared void fail(String message) {
    assert(false, message);
}

shared void results() {
    print("assertions " assertionCount 
          ", failures " failureCount "");
}

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

shared void test() {
    value enums = {1, 2.0};
    //is cases
    switch(enums[0])
    case(is Integer) {}
    else { fail("FLOAT? WTF?"); }

    switch(enums[1])
    case(is Integer) { fail("INTEGER? WTF?"); }
    case(is Float) {}
    else { fail("Nothing!!! WTF?"); }

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
        assert(result=="int", "is/continue");
    }

    //matches
    assert(testMatch1(testcase1)=="case1", "match1");
    assert(testMatch1(testcase2)=="case2", "match 2");
    assert(testMatch1(testcase3)=="case3", "match 3");
    assert(testMatch2(testcase1)=="case 1|2", "match 4");
    assert(testMatch2(testcase2)=="case 1|2", "match 5");
    assert(testMatch2(testcase3)=="case 3", "match 6");

    //is
    assert(testIs1(1) == 2, "is1");
    assert(testIs1(1.5) == 2.5, "is2");

    //TODO satisfies

    results();
}


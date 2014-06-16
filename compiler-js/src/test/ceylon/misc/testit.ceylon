import members { Counter }
import check { ... }

shared void test() {
    value name = "hello";
    Foo foo = Foo("goodbye");
    check(printBoth(name, foo.name)=="hello, goodbye");
    check(printBoth{y="y"; x="x";}=="x, y");
    foo.inc(); foo.inc();
    check(foo.count == 3, "Foo.count");
    check(foo.string == "Foo(goodbye)", "Foo.string");
    check(foo.printName() == "foo.name=goodbye");
    check(Bar().printName() == "bar.name=Hello,foo.name=Hello,foo.name=Hello");
    check("``Bar().Inner()``"=="creating inner class of: Hello");
    doIt(foo.inc);
    check(foo.count == 5, "Foo.count [2]");
    doIt(Bar);
    print(foob.name);
    object x {
        shared String y() {
            return "xy";
        }
    }
    check(x.y()=="xy");
    Bar b = Bar();
    b.Inner().incOuter();
    b.Inner().incOuter();
    b.Inner().incOuter();
    check(b.count == 4, "Bar.count");
    printAll("hello", "world");
    printAll{strings=["hello","world"];};
    
    Counter c = Counter(0);
    c.inc(); c.inc();
    check(c.count == 2, "Counter.count");
    
    value v2 = var();
    test_objects();
    testAliasing();
    testLate();
    testReifiedRuntime();
    testStackTrace();
    results();
}

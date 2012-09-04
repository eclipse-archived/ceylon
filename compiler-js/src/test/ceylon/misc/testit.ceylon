import members { Counter }
import assert { ... }

shared void test() {
    value name = "hello";
    print(name);
    Foo foo = Foo("goodbye");
    printBoth(name, foo.name);
    printBoth{y="y"; x="x";};
    foo.inc(); foo.inc();
    assert(foo.count == 3, "Foo.count");
    assert(foo.string == "Foo(goodbye)", "Foo.string");
    foo.printName();
    Bar().printName();
    Bar().Inner();
    doIt(foo.inc);
    assert(foo.count == 5, "Foo.count [2]");
    doIt(Bar);
    print(foob.name);
    object x {
        shared void y() {
            print("xy");
        }
    }
    x.y();
    Bar b = Bar();
    b.Inner().incOuter();
    b.Inner().incOuter();
    b.Inner().incOuter();
    assert(b.count == 4, "Bar.count");
    printAll("hello", "world");
    printAll{"hello", "world"};
    
    Counter c = Counter(0);
    c.inc(); c.inc();
    assert(c.count == 2, "Counter.count");
    
    value v2 = var();
    test_objects();
    testAliasing();
    results();
}

import members { Counter }

shared void testit() {
    value name = "hello";
    print(name);
    F foo = F("goodbye");
    printBoth(name, foo.name);
    printBoth{y="y"; x="x";};
    foo.inc(); foo.inc();
    print(foo.count);
    foo.printName();
    Bar().printName();
    Bar().Inner();
    doIt(foo.inc);
    print(foo.count);
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
    print(b.count);
    printAll("hello", "world");
    printAll{"hello", "world"};
    
    Counter c = Counter(0);
    c.inc(); c.inc();
    print(c.count);
}

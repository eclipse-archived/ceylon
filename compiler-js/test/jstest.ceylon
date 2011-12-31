interface X {
    shared void helloWorld() {
       print("hello world");
    }
}

class Foo(String name) {
    shared String name = name;
    variable value counter:=0;
    shared Integer count { return counter; }
    shared void inc() { counter:=counter+1; }
    shared default void printName() {
        print("foo name = " + name);
    }
    inc();
}

class Bar() extends Foo("Hello") satisfies X {
    shared actual void printName() {
        print("bar name = " + name);
        super.printName();
    }
    shared class Inner() {
        print("creating inner class of :" 
            + outer.name);
        shared void incOuter() {
            inc();
        }
    }
}

void printBoth(String x, String y) {
    print(x + ", " + y);
}

void doIt(void f()) {
    f(); f();
}

object foob {
    shared String name="Gavin";
}

void printAll(String... strings) {}

void testit() {
    value name = "hello";
    print(name);
    Foo foo = Foo("goodbye");
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
}

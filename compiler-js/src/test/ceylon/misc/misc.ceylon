shared interface X {
    shared void helloWorld() {
       print("hello world");
    }
}

shared class Foo(name) {
    shared String name;
    variable value counter=0;
    shared Integer count { return counter; }
    shared void inc() { counter=counter+1; }
    shared default void printName() {
        print("foo name = " + name);
    }
    shared default actual String string = "Foo(``name``)";
    inc();
}

shared class Bar() extends Foo("Hello") satisfies X {
    shared actual void printName() {
        print("bar name = " + name);
        (super of Foo).printName();
        super.printName();
    }
    shared class Inner() {
        print("creating inner class of :" 
            + outer.name);
        shared void incOuter() {
            inc();
        }
    }
    //this.printName();
    //super.printName();
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

void printAll(String* strings) {}

class F(String name) => Foo(name);

shared Integer var() { return 5; }

import check { check }
import members { Class656, Interface656 }

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
    shared default String printName() =>
        "foo.name=" + name;
    shared default actual String string = "Foo(``name``)";
    inc();
}

shared class Bar() extends Foo("Hello") satisfies X {
    shared actual String printName() {
        return "bar.name=" + name + ","
            + (super of Foo).printName() +  ","
            + super.printName();
    }
    shared class Inner() {
        shared actual String string = "creating inner class of: " 
            + outer.name;
        shared void incOuter() {
            inc();
        }
    }
}

String printBoth(String x, String y) {
    return x + ", " + y;
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

//Issue #249
{Integer*} container249 = [object249.int];
shared object object249{
  shared Integer int = 1;
}

//For testing of 461
shared class TestClass461 {
  shared new testConstructor() {}
  shared void test() => check(true, "Import toplevel constructor");
}

shared void testBugCl733(){
    // https://github.com/ceylon/ceylon.language/issues/733
    value ignore = Array(1..4).keys.reversed;

    BugCl733Class().One().Two();
    BugCl733Class().OneInterface().TwoInterface();
}

shared class BugCl733Class() satisfies BugCl733Interface {
    shared class One() extends BugCl733Class(){}
    shared class Two() extends BugCl733Class(){}
}
shared interface BugCl733Interface {
    shared class OneInterface() satisfies BugCl733Interface{}
    shared class TwoInterface() satisfies BugCl733Interface{}
}

void test656() {
  check((Class656().Inner656() of Object) is Class656.Inner656, "656.1");
  check(!(Class656().Inner656() of Object) is Class656.Iface656, "656.2");
  class MyClass() satisfies Interface656 {
    shared Object foo => object satisfies Iface656{};
  }
  check((MyClass().Inner656() of Object) is Interface656.Inner656, "656.3");
  check(MyClass().foo is Interface656.Iface656, "656.4");
}

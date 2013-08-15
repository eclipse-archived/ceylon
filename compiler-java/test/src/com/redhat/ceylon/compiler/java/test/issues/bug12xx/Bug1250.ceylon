void bug1250() {
    class Foo() { 
        shared class Bar() {}
    }
    Foo foo = Foo();
    value bar2 = foo.Bar();
}
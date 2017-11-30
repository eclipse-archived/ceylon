shared class Foo {
    shared restricted new () {}
    shared restricted new foo() {}
}

shared void run() {
    Foo();
    Foo.foo();    
}
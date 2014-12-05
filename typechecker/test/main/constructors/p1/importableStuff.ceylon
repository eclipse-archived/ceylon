shared class Foo {
    shared new New() {}
}

shared object foo {
    shared String bar = "Hello";
}

shared object bar {
    shared class Bar(shared String bar) {}
}
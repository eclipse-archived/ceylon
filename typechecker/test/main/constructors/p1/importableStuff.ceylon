shared class Foo {
    shared new New() {}
}

shared class Generic<T> {
    shared new Broken(T t) {}
}

shared object foo {
    shared String bar = "Hello";
}

shared object bar {
    shared class Bar(shared String bar) {}
    @type:"bar.Bar" Bar b = bar.Bar("");
    @type:"bar.Bar" bar.Bar("");
}
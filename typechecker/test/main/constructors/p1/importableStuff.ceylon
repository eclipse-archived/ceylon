import constructors.p1 { Imported { create } }

shared class Foo {
    shared new create() {}
}

shared class Generic<T> {
    shared new broken(T t) {}
}

shared class Imported<T> {
    shared new create(T t) {}
    void method() {
        @type:"Imported<T>"
        value gen = create(nothing);
        @error
        value oops = create("");
    }
}

shared object foo {
    shared String bar = "Hello";
}

shared object bar {
    shared class Bar(shared String bar) {}
    @type:"bar.Bar" Bar b = bar.Bar("");
    @type:"bar.Bar" bar.Bar("");
}
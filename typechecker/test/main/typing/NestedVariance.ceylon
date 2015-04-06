abstract class Co1<out T>(T t) {
    shared class Foo() {
        shared T get() { return t; } 
    } 
    shared void bar(@error Foo foo) {
        T t = foo.get();
    }
}

abstract class Co2<out T>(T t) {
    shared class Foo() {
        shared T get() { return t; } 
    } 
    shared void bar(@error Co2<T>.Foo foo) {
        T t = foo.get();
    }
}

interface Cov<out T> {
    shared formal T t;
}

class FooBar<in T>(T t) {
    @error shared object cov satisfies Cov<T> {
        t => outer.t;
    }
    shared object obj {
        @error shared T t => outer.t; 
    }
    @error value ecov = object satisfies Cov<T> {
        t => outer.t;
    };
    value eobj = object {
        @error shared T t => outer.t; 
    };
}
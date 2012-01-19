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
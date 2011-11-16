@nomodel
interface Common {
    shared formal void common();
}
@nomodel
class Foo() satisfies Common {
    shared actual void common() {}
    shared void bar() {}
}
@nomodel
class FooSub() extends Foo() {
    shared void baz() {}
}
@nomodel
class Bar() satisfies Common {
    shared actual void common() {}
    shared void foo() {}
}
@nomodel
class MethodIfIs() {
    shared void m(Object x) {
        if (is Foo x) {
            x.bar();
            if (is FooSub x) {
                x.baz();
            }
        }
        if (is Foo|Bar x) {
            x.common();
        }
        if (is Foo&Bar x) {
            x.bar();
            x.foo();
        }
        if (is FooSub y = give()) {
            y.bar();
        }
    }
    Foo give() {
        return FooSub();
    }
}

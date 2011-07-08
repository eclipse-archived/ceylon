@nomodel
class Foo() {
    shared void bar() {}
}
@nomodel
class FooSub() extends Foo() {
    shared void baz() {}
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
        if (is FooSub y = give()) {
            y.bar();
        }
    }
    Foo give() {
        return FooSub();
    }
}

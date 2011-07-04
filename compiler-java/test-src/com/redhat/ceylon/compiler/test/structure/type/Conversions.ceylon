@nomodel
class Foo() {
    shared void a() { }
}
@nomodel
class Bar() extends Foo() {
    shared void b() { }
}
@nomodel
class Baz() extends Foo() {
    shared void c() { }
}
@nomodel
class Conversions() {
    shared void m() {
        Foo c1 = Foo();
        c1.a();
        Bar|Baz u1 = Bar();
        u1.a();
        if (is Bar u1) {
            u1.a();
            u1.b();
        }
    }
}

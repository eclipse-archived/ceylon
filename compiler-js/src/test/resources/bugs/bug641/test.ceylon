deprecated class A() { }

suppressWarnings("deprecation")
interface I {
    shared A a => A();
}

suppressWarnings("deprecation")
class C() {
    shared A a => A();
}

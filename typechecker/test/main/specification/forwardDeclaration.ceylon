abstract class A() {
    shared void f() {
        neverDefined();
    }
    @error void neverDefined();
}

class AA() extends A() {}

void f() {
    AA aa = AA();
    aa.f();
}

void a() {
    void f() {
        @error neverDefined();
    }
    void neverDefined();
}
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

class B() {
    String name;
    name => "gavin";
    @error assign name {}
}

class C() {
    shared void foo() { 
        @error print(bar); 
    }
    String bar;
    bar => "hello1";
    void init() {}
}

class D() {
    late String name;
    @error name => "gavin";
}
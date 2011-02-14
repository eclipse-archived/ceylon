class Primaries() {
    class A() {
        shared B b = B();
    }
    class B() {
        shared C c() = C();
    }
    class C() {}
    
    @type["A"] A();
    
    @type["A"] A a = A();
    
    @type["B"] a.b;
    
    @type["C"] a.b.c();
    
}
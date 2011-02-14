class Primaries() {
    
    class A() {
        shared B b = B();
    }
    class B() {
        shared C c() = C();
    }
    class C() {}
    
    @type["A"] A();
    
    @error A().x;
    
    @error A().c();
    
    @type["A"] (A());
    
    @type["A"] A aa = A();
    
    @error aa.c();

    @type["B"] aa.b;
    
    @type["B"] local bb = aa.b;
    
    @error bb.b;
    
    @type["C"] aa.b.c();
    
    @type["C"] (aa.b).c();
    
    @type["C"] local cc { return bb.c(); }
    
    @type["C"] local ccc { return aa.b.c(); }
    
    @type["A"] this.A();
    
    //@type["String"] "Hello";
    
    class Inner(A arg) {
        @type["A"] arg;
        @type["A"] aa;
        @type["C"] cc;
        @type["Primaries"] outer;
        @type["Inner"] this;
        shared class Inner2() {
            @type["Inner2"] this;
            @type["Inner"] outer;
            @type["Primaries"] outer.outer;
        }
    }
    
    @type["Inner2"] Inner(aa).Inner2();
    @type["Inner"] Inner(A()).Inner2().outer;
    @type["Primaries"] Inner(aa).Inner2().outer.outer;
    
    void method(A arg1, B arg2) {
        @type["A"] arg1;
        @type["B"] arg2;
        @type["A"] aa;
        @type["C"] cc;
        @type["Primaries"] this;
        class Inner3() {
            @type["Inner3"] this;
            @type["Primaries"] outer;
        }
        @type["Inner3"] Inner3();
        @type["Primaries"] Inner3().outer;
    }
    
}
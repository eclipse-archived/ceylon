class Primaries() {
    
    class A() {
        shared B b = B();
    }
    class B() {
        shared C c() = C();
    }
    class C() {}
    
    @type["A"] A();
    
    @type["Sequence<A>"] { A(), A() };
    
    @error A().x;
    
    @error A().c();
    
    @type["A"] (A());
    
    @error A()();
    
    @type["A"] A aa = A();
    
    @error aa.c();
    
    @error aa.b();

    @type["B"] aa.b;
    
    @type["B"] local bb = aa.b;
    
    @error bb.b;
    
    @type["C"] bb.c();
    
    @error bb.c()();
    
    @type["C"] aa.b.c();
    
    @type["C"] (aa.b).c();
    
    @type["C"] local cc { return bb.c(); }
    
    @type["C"] local ccc { return aa.b.c(); }
    
    @type["A"] this.A();
    
    @type["A"] Primaries().A();
    
    @type["String"] "Hello";
    
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
    //@type["A"] Inner(A()).aa;
    
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
    
    interface G {}
    interface H {}
    class S() extends B() satisfies G & H {}
    class T() satisfies G & H {}
    
    @type["Sequence<B>"] {S(), B()};
    @type["Sequence<B>"] {B(), S()};
    @type["Sequence<S|T>"] {S(), T()};
    @type["Sequence<T|S>"] {T(), S()};
    B[] bs1 = {S(), B()};
    B[] bs2 = {B(), S()};
    H[] hs = {S(), T()};
    G[] gs = {T(), S()};
    @type["Sequence<A|B|String|Sequence<Natural>>"] {A(),B(),"Hello",A(),{1,2,3}, S()};
    Object[] stuff = {A(),B(),"Hello",{1,2,3}};
    local objects = {A(),B(),"Hello",{1,2,3}};
    Object[] things = objects;
    @type["Optional<A|B|String|Sequence<Natural>>"] objects[1];
    @type["Optional<String>"] objects[1]?.string;
    if (exists local o = objects[1]) {
        @type["A|B|String|Sequence<Natural>"] o;
        String s = o.string;
        @type["Sequence<A|B|String|Sequence<Natural>|Float>"] {o, "foo", 3.1, A()};
    }
    
}
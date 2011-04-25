class Primaries() {
    
    class C() extends IdentifiableObject() {}
    
    class B() extends IdentifiableObject() {
        shared C c() = C();
    }
    
    class A() extends IdentifiableObject() {
        shared B b = B();
    }

    @type["Primaries.A"] local x1 = A();
    
    @type["Sequence<Primaries.A>"] local p2 = { A(), A() };
    
    @error local p3 = A().x;
    
    @error local p4 = A().c();
    
    @type["Primaries.A"] local p5 = (A());
    
    @error local p6 = A()();
    
    @type["Primaries.A"] A aa = A();
    
    @error local p7 = aa.c();
    
    @error local p8 = aa.b();

    @type["Primaries.B"] local p9 = aa.b;
    
    @type["Primaries.B"] local bb = aa.b;
    
    @error local p10 = bb.b;
    
    @type["Primaries.C"] local p11 = bb.c();
    
    @error local p12 = bb.c()();
    
    @type["Primaries.C"] local p13 = aa.b.c();
    
    @type["Primaries.C"] local p14 = (aa.b).c();
    
    @type["Primaries.C"] local cc { return bb.c(); }
    
    @type["Primaries.C"] local ccc { return aa.b.c(); }
    
    @type["Primaries.A"] local p15 = this.A();
    
    @error A().this;
    @error A().super;
    
    @type["Primaries.A"] local p16 = Primaries().A();
    
    @type["String"] local p17 = "Hello";
    
    class Inner(A arg) {
        @type["Primaries.A"] local p1 = arg;
        @type["Primaries.A"] local p2 = aa;
        @type["Primaries.C"] local p3 = cc;
        @type["Primaries"] local p4 = outer;
        @type["Primaries.Inner"] local p5 = this;
        shared class Inner2() {
            @type["Primaries.Inner.Inner2"] local p6 = this;
            @type["Primaries.Inner"] local p7 = outer;
            @type["Primaries"] local p8 = outer.outer;
        }
    }
    
    @type["Primaries.Inner.Inner2"] local p18 = Inner(aa).Inner2();
    @type["Primaries.Inner"] local p19 = Inner(A()).Inner2().outer;
    @type["Primaries"] local p20 = Inner(aa).Inner2().outer.outer;
    //@type["A"] Inner(A()).aa;
    
    void method(A arg1, B arg2) {
        @type["Primaries.A"] local p1 = arg1;
        @type["Primaries.B"] local p2 = arg2;
        @type["Primaries.A"] local p3 = aa;
        @type["Primaries.C"] local p4 = cc;
        @type["Primaries"] local p5 = this;
        class Inner3() {
            @type["Inner3"] local p6 = this;
            @type["Primaries"] local p7 = outer;
        }
        @type["Inner3"] local p8 = Inner3();
        @type["Primaries"] local p9 = Inner3().outer;
    }
    
    interface G satisfies Equality {
        shared String getIt() {
            return "Hello";
        }
    }
    interface H satisfies Equality {
        shared void doIt() {}
    }
    class S() extends B() satisfies G & H {}
    class T() extends IdentifiableObject() satisfies G & H {}
    
    @type["Sequence<Primaries.B>"] local p21 = {S(), B()};
    @type["Sequence<Primaries.B>"] local p22 = {B(), S()};
    @type["Sequence<Primaries.S|Primaries.T>"] local p23 = {S(), T()};
    @type["Sequence<Primaries.T|Primaries.S>"] local p24 = {T(), S()};
    {T(), S()}[].doIt();
    @type["Sequence<String>"] local p25 = {S(), T()}[].getIt();
    B[] bs1 = {S(), B()};
    B[] bs2 = {B(), S()};
    //H[] hs = {S(), T()};
    //G[] gs = {T(), S()};
    @type["Sequence<Primaries.A|Primaries.B|String|Sequence<Natural>>"] local p26 = {A(),B(),"Hello",A(),{1,2,3}, S()};
    //Object[] stuff = {A(),B(),"Hello",{1,2,3}};
    local objects = {A(),B(),"Hello",{1,2,3}};
    //Object[] things = objects;
    @type["Nothing|Primaries.A|Primaries.B|String|Sequence<Natural>"] local p27 = objects[1];
    @type["Nothing|String"] local p28 = objects[1]?.string;
    if (exists local o = objects[1]) {
        @type["Primaries.A|Primaries.B|String|Sequence<Natural>"] local p29 = o;
        String s = o.string;
        @type["Sequence<Primaries.A|Primaries.B|String|Sequence<Natural>|Float>"] local p30 = {o, "foo", 3.1, A()};
    }
    @type["Empty|Sequence<String>"] String[] noStrings = {};
    @type["Empty"] local none = {};
    
}
class Primaries() {
    
    class C() extends IdentifiableObject() {}
    
    class B() extends IdentifiableObject() {
        shared C c() { return C(); }
    }
    
    class A() extends IdentifiableObject() {
        shared B b = B();
    }

    @type["Primaries.A"] value x1 = A();
    
    @type["Sequence<Primaries.A>"] value p2 = { A(), A() };
    
    @error value p3 = A().x;
    
    @error value p4 = A().c();
    
    @type["Primaries.A"] value p5 = (A());
    
    @error value p6 = A()();
    
    @type["Primaries.A"] A aa = A();
    
    @error value p7 = aa.c();
    
    @error value p8 = aa.b();

    @type["Primaries.B"] value p9 = aa.b;
    
    @type["Primaries.B"] value bb = aa.b;
    
    @error value p10 = bb.b;
    
    @type["Primaries.C"] value p11 = bb.c();
    
    @error value p12 = bb.c()();
    
    @type["Primaries.C"] value p13 = aa.b.c();
    
    @type["Primaries.C"] value p14 = (aa.b).c();
    
    @type["Primaries.C"] value cc { return bb.c(); }
    
    @type["Primaries.C"] value ccc { return aa.b.c(); }
    
    @type["Primaries.A"] value p15 = this.A();
    
    @error A().this;
    @error A().super;
    
    @type["Primaries.A"] value p16 = Primaries().A();
    
    @type["String"] value p17 = "Hello";
    
    class Inner(A arg) {
        Natural n = 0;
        @type["Primaries.A"] value p1 = arg;
        @type["Primaries.A"] value p2 = aa;
        @type["Primaries.C"] value p3 = cc;
        @type["Primaries.A"] value p4 = outer.x1;
        void inner() {
            @type["Primaries.Inner"] value p5 = this;
        }
        shared class Inner2() {
            @type["Natural"] value p7 = outer.n;
            @type["Primaries.Inner"] value p8 = outer;
            void inner() {
                @type["Primaries.Inner.Inner2"] value p6 = this;
            }
            //@type["Primaries"] value p8 = outer.outer;
        }
    }
    
    @type["Primaries.Inner.Inner2"] value p18 = Inner(aa).Inner2();
    //@type["Primaries.Inner"] value p19 = Inner(A()).Inner2().outer;
    //@type["Primaries"] value p20 = Inner(aa).Inner2().outer.outer;
    //@type["A"] Inner(A()).aa;
    
    void method(A arg1, B arg2) {
        @type["Primaries.A"] value p1 = arg1;
        @type["Primaries.B"] value p2 = arg2;
        @type["Primaries.A"] value p3 = aa;
        @type["Primaries.C"] value p4 = cc;
        @type["Primaries.A"] value p5 = this.x1;
        class Inner3() {
            String s = "hello";
            @type["String"] value p6 = this.s;
            @type["Primaries.A"] value p7 = outer.x1;
            void inner() {
                @type["Inner3"] value t = this;
            }
        }
        @type["Inner3"] value p8 = Inner3();
        //@type["Primaries"] value t = this;
        //@type["Primaries"] value p9 = Inner3().outer;
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
    
    @type["Sequence<Primaries.B>"] value p21 = {S(), B()};
    @type["Sequence<Primaries.B>"] value p22 = {B(), S()};
    @type["Sequence<Primaries.S|Primaries.T>"] value p23 = {S(), T()};
    @type["Sequence<Primaries.T|Primaries.S>"] value p24 = {T(), S()};
    {T(), S()}[].doIt();
    @type["Sequence<String>"] value p25 = {S(), T()}[].getIt();
    B[] bs1 = {S(), B()};
    B[] bs2 = {B(), S()};
    //H[] hs = {S(), T()};
    //G[] gs = {T(), S()};
    @type["Sequence<Primaries.A|Primaries.B|String|Sequence<Natural>>"] value p26 = {A(),B(),"Hello",A(),{1,2,3}, S()};
    //Object[] stuff = {A(),B(),"Hello",{1,2,3}};
    value objects = {A(),B(),"Hello",{1,2,3}};
    //Object[] things = objects;
    @type["Nothing|Primaries.A|Primaries.B|String|Sequence<Natural>"] value p27 = objects[1];
    @type["Nothing|String"] value p28 = objects[1]?.string;
    if (exists o = objects[1]) {
        @type["Primaries.A|Primaries.B|String|Sequence<Natural>"] value p29 = o;
        String s = o.string;
        @type["Sequence<Primaries.A|Primaries.B|String|Sequence<Natural>|Float>"] value p30 = {o, "foo", 3.1, A()};
    }
    @type["Empty|Sequence<String>"] String[] noStrings = {};
    @type["Empty"] value none = {};
    
}
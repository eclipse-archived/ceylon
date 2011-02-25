class Primaries() {
    
    class A() extends IdentifiableObject() {
        shared B b = B();
    }
    class B() extends IdentifiableObject() {
        shared C c() = C();
    }
    class C() extends IdentifiableObject() {}
    
    @type["Primaries.A"] A();
    
    @type["Sequence<Primaries.A>"] { A(), A() };
    
    @error A().x;
    
    @error A().c();
    
    @type["Primaries.A"] (A());
    
    @error A()();
    
    @type["Primaries.A"] A aa = A();
    
    @error aa.c();
    
    @error aa.b();

    @type["Primaries.B"] aa.b;
    
    @type["Primaries.B"] local bb = aa.b;
    
    @error bb.b;
    
    @type["Primaries.C"] bb.c();
    
    @error bb.c()();
    
    @type["Primaries.C"] aa.b.c();
    
    @type["Primaries.C"] (aa.b).c();
    
    @type["Primaries.C"] local cc { return bb.c(); }
    
    @type["Primaries.C"] local ccc { return aa.b.c(); }
    
    @type["Primaries.A"] this.A();
    
    @type["Primaries.A"] Primaries().A();
    
    @type["String"] "Hello";
    
    class Inner(A arg) {
        @type["Primaries.A"] arg;
        @type["Primaries.A"] aa;
        @type["Primaries.C"] cc;
        @type["Primaries"] outer;
        @type["Primaries.Inner"] this;
        shared class Inner2() {
            @type["Primaries.Inner.Inner2"] this;
            @type["Primaries.Inner"] outer;
            @type["Primaries"] outer.outer;
        }
    }
    
    @type["Primaries.Inner.Inner2"] Inner(aa).Inner2();
    @type["Primaries.Inner"] Inner(A()).Inner2().outer;
    @type["Primaries"] Inner(aa).Inner2().outer.outer;
    //@type["A"] Inner(A()).aa;
    
    void method(A arg1, B arg2) {
        @type["Primaries.A"] arg1;
        @type["Primaries.B"] arg2;
        @type["Primaries.A"] aa;
        @type["Primaries.C"] cc;
        @type["Primaries"] this;
        class Inner3() {
            @type["Inner3"] this;
            @type["Primaries"] outer;
        }
        @type["Inner3"] Inner3();
        @type["Primaries"] Inner3().outer;
    }
    
    interface G satisfies Equality<IdentifiableObject> {
        shared String getIt() {
            return "Hello";
        }
    }
    interface H satisfies Equality<IdentifiableObject> {
        shared void doIt() {}
    }
    class S() extends B() satisfies G & H {}
    class T() extends IdentifiableObject() satisfies G & H {}
    
    @type["Sequence<Primaries.B>"] {S(), B()};
    @type["Sequence<Primaries.B>"] {B(), S()};
    @type["Sequence<Primaries.S|Primaries.T>"] {S(), T()};
    @type["Sequence<Primaries.T|Primaries.S>"] {T(), S()};
    {T(), S()}[].doIt();
    @type["Sequence<String>"] {S(), T()}[].getIt();
    B[] bs1 = {S(), B()};
    B[] bs2 = {B(), S()};
    //H[] hs = {S(), T()};
    //G[] gs = {T(), S()};
    @type["Sequence<Primaries.A|Primaries.B|String|Sequence<Natural>>"] {A(),B(),"Hello",A(),{1,2,3}, S()};
    //Object[] stuff = {A(),B(),"Hello",{1,2,3}};
    local objects = {A(),B(),"Hello",{1,2,3}};
    //Object[] things = objects;
    @type["Optional<Primaries.A|Primaries.B|String|Sequence<Natural>>"] objects[1];
    @type["Optional<String>"] objects[1]?.string;
    if (exists local o = objects[1]) {
        @type["Primaries.A|Primaries.B|String|Sequence<Natural>"] o;
        String s = o.string;
        @type["Sequence<Primaries.A|Primaries.B|String|Sequence<Natural>|Float>"] {o, "foo", 3.1, A()};
    }
    String[] noStrings = none;
    
}
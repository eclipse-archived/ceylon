interface InitializerParams {

    class Baz(foo, bar) {
        String foo;
        shared String bar;
        print(foo);
        print(bar);
    }

    class Foo(foo, bar, @error fum, qux, fo, fi, fee, lorax,
            xarol, @error twiddle, @error twaddle) {
        String foo;
        shared String bar;
        String qux();
        @error value fo;
        @error function fi();
        if (true) {
            String twiddle;
        }
        @error String fee = "goodbye";
        String twaddle { return "hello"; }
        String lorax;
        if (false) {
            @error lorax = "something";
        }
        variable String xarol;
        if (true) {
            xarol := "something else";
        }
    }

    abstract class Bar(foo, bar, @error baz, @error fum) {
        String foo;
        shared String bar;
        shared formal String baz;
    }

    void foo(foo, @error fum, qux, fo, fi, fee, lorax,
            xarol, @error twiddle, @error twaddle) {
        String foo;
        String qux();
        @error value fo;
        @error function fi();
        if (true) {
            String twiddle;
        }
        @error String fee = "goodbye";
        String twaddle { return "hello"; }
        String lorax;
        if (false) {
            @error lorax = "something";
        }
        variable String xarol;
        if (true) {
            xarol := "something else";
        }
    }
    
    void bar(foo) {
        String foo;
        print(foo);
    }
    
    class Func(f) {
        void f(Object o);
        f(0);
    }

    void func(f) {
        String f();
        print(f());
    }

    void test() {
        Baz("", "");
        bar("");
        @error Baz(0);
        @error baz(0);
        Func(print);
        func(() "hello");
        @error Func(() "hello");
        @error func(print);
    }
    
    class A(@error i) {
        @error print(i);
        shared default Integer i;
        @error print(i);
    }
    
    class WithSharedParams(shared String message, 
            shared Float fun(Float x)) {
        print(message + fun(0.0).string);
    }
    class WithSharedDefaultAtts() {
        shared default String message=""; 
        shared default Float fun(Float x) => 0.0;
    }
    class WithSharedActualParams(shared actual String message, 
            shared actual Float fun(Float x)) 
            extends WithSharedDefaultAtts() {
        print(message + fun(0.0).string);
    }
    class WithSharedActualAtts() 
            extends WithSharedDefaultAtts() {
        shared actual String message => "bye";
        shared actual Float fun(Float x)=>-x;
    }
    abstract class WithSharedFormalParams(@error shared formal String message, 
            @error shared formal Float fun(Float x)) {}
    class WithSharedDefaultParams(@error shared default String message, 
            @error shared default Float fun(Float x)) {}
    void testWithSharedParams() {
        WithSharedParams wsp = WithSharedParams("hello", (Float x)=>x*2.0);
        print(wsp.message);
        print(wsp.fun(1.0));
        WithSharedActualParams wsap = WithSharedActualParams("hello", (Float x)=>x*2.0);
        print(wsap.message);
        print(wsap.fun(1.0));
    }
    
    abstract class WithFormalDefaultParams(@error name, @error count) {
        shared formal String name;
        shared default Integer count;
    }
    
    class Super() {
        shared String str = "str";
    }
    class SubWithParam(String str) extends Super() {}
    class SubWithAtt() extends Super() { String str=""; }
    
}
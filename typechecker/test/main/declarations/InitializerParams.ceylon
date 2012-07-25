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
    
    class A(i) {
        @error print(i);
        shared default Integer i;
        @error print(i);
    }
    
}
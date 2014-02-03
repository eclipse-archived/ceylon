interface InitializerParams {

    class Baz(foo, bar) {
        String foo;
        shared String bar;
        print(foo);
        print(bar);
    }

    @error class Foo(foo, bar, fum, qux, fo, fi, fee, lorax,
            xarol, twiddle, twaddle) {
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
            xarol = "something else";
        }
    }

    @error abstract class Bar(foo, bar, baz, fum) {
        String foo;
        shared String bar;
        shared formal String baz;
    }

    @error void foo(foo, fum, qux, fo, fi, fee, lorax,
            xarol, twiddle, twaddle) {
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
            xarol = "something else";
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
        func(() => "hello");
        @error Func(() => "hello");
        @error func(print);
    }
    
    class A(i) {
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
    class WithSharedDefaultParams(shared default String message, 
            shared default Float fun(Float x)) {
        @error print(message + fun(0.0).string);
    }
    class WithSharedActualParams2(shared actual String message, 
            shared actual Float fun(Float x))
            extends WithSharedDefaultParams(message, fun) {
        print(message + fun(0.0).string);
    }
    void testWithSharedParams() {
        WithSharedParams wsp = WithSharedParams("hello", (Float x)=>x*2.0);
        print(wsp.message);
        print(wsp.fun(1.0));
        WithSharedActualParams wsap = WithSharedActualParams("hello", (Float x)=>x*2.0);
        print(wsap.message);
        print(wsap.fun(1.0));
        WithSharedActualParams2 wsap2 = WithSharedActualParams2("hello", (Float x)=>x*2.0);
        print(wsap2.message);
        print(wsap2.fun(1.0));
    }
    
    @error abstract class WithFormalDefaultParams(name, count) {
        shared formal String name;
        shared default Integer count;
    }
    
    class Super() {
        shared String str = "str";
    }
    class SubWithParam(String str) extends Super() {}
    class SubWithAtt() extends Super() { String str=""; }
    
    class XX(shared default Float x) {}
    class YY(x) { shared default Float x; }
    
    class WithSequencedParam(strings) {
        String* strings;
        for (String s in strings) {}
    }
    void withSequencedParam(strings) {
        String* strings;
        for (String s in strings) {}
    }
    void broken(@error Float* floats()) {
        @error Integer* ints;
        @error String* strings() { return ["hello"]; }
    }
    
    void testSequencedParam() {
        WithSequencedParam();
        WithSequencedParam("hello", "world");
        withSequencedParam();
        withSequencedParam("hello", "world");
    }
    
    class TestNewSyntax(name, proc) {
        shared String proc(String s);
        shared String name;
    }
    
    @error class Qux(bar){ void bar<T>(); }
    
    @error shared class BrokenVis(baz) {
        Baz baz;
    }
    shared class BrokenVar<out T>() {
        shared void method(t, lt) {
            @error T t; 
            @error List<T> lt;
        }
    }
    class Superclass() {
        shared default void m(s, o, i) {
            String s; Object o; Integer i;
        }
    }
    class Subclass() extends Superclass() {
        @error shared actual void m(s, o, i) {
            Object s; String o; Integer i;
        }
    }
    
    @error class XXXX(i=1.0) { Integer i; }

}

class WithGoodFunParam1(eq = (Object that) => true) {
    Boolean eq(Object that);
}
class WithGoodFunParam2(Boolean eq(Object that) => true) {}


@error class WithBadFunParam1(eq = (Object that) => 1) {
    Boolean equals(Object that);
}
class WithBadFunParam2(@error Boolean eq(Object that) => 1) {}

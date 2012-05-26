interface InitializerParams {

    class Baz(foo, bar) {
        String foo;
        shared String bar;
        print(foo);
        print(bar);
    }

    class Foo(foo, bar, @error fum, @error qux) {
        String foo;
        shared String bar;
        String qux();
    }

    abstract class Bar(foo, bar, @error baz, @error fum) {
        String foo;
        shared String bar;
        shared formal String baz;
    }

    void foo(foo, @error fum, @error qux) {
        String foo;
        String qux();
    }
    
    void bar(foo) {
        String foo;
        print(foo);
    }
    
    void test() {
        Baz("", "");
        bar("");
        @error Baz(0);
        @error baz(0);
    }

}
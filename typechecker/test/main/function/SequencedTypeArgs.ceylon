class Foo<Arg...>() {}

void test() {
    Foo foo0 = Foo();
    Foo<String> foo1 = Foo<String>();
    Foo<String,Integer> foo2 = Foo<String,Integer>();
    
    @error Foo<String> err1 = foo2;
    @error Foo<String,Integer> err2 = foo1;
    @error Foo err3 = foo1;
    @error Foo<String> err4 = foo0;
    
    Foo good0 = foo0; 
    Foo<String> good1 = foo1;
    Foo<String,Integer> good2 = foo2;
    
    void bar1(Foo<String> foo) {}
    void bar2(Foo<String,Integer> foo) {}
    
    bar1(foo1);
    bar2(foo2);
    @error bar1(foo2);
    @error bar2(foo1);
}
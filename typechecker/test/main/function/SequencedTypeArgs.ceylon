class Foo<Arg>() given Arg satisfies Sequential<Anything> {}
class Bar<in Arg>() given Arg satisfies Sequential<Anything> {}

void test() {
    Foo<Empty> foo0 = Foo<Empty>();
    Foo<[String]> foo1 = Foo<[String]>();
    Foo<[String,Integer]> foo2 = Foo<[String,Integer]>();
    
    @error Foo<[String]> err1 = foo2;
    @error Foo<[String,Integer]> err2 = foo1;
    @error Foo<Empty> err3 = foo1;
    @error Foo<[String]> err4 = foo0;
    
    Foo<Empty> good0 = foo0; 
    Foo<[String]> good1 = foo1;
    Foo<[String,Integer]> good2 = foo2;
    
    void bar1(Foo<[String]> foo) {}
    void bar2(Foo<[String,Integer]> foo) {}
    
    bar1(foo1);
    bar2(foo2);
    @error bar1(foo2);
    @error bar2(foo1);
    
    Bar<[String,String]> bar = Bar<[Object,Object]>();
    @error Foo<[String,String]> bar = Foo<[Object,Object]>();
}
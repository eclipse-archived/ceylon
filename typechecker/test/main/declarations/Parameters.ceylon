class Parameters() {
    void x1(@type["String"] String s) {}
    void x2(@type["String"] String s, @type["Integer"] Integer n) {}
    void x3(@type["Empty|Sequence<String>"] String[] s) {}
    void x4(@type["Nothing|String"] String? s) {}
    void x5(@type["Iterable<String>"] String... s) {}
    
    void x6(String s="hello", @error Integer n) {}
    void x7(String... s, @error Integer n) {}
    void x8(String... s, @error Integer n=0) {}
    
    void d1(String name="World") {}
    void d2(Integer count=0) {}
    void d3(@error String name=0) {}
    void d4(@error Integer count="World") {}
    void d5(String? name=null) {}
    void d6(String? name="World") {}
    void d7(@error variable String s) {}
    
    void broken1(@error Unknown p) {}
    @error broken1("hello");
    void broken2<T>(T t, @error Unknown p) {}
    @error broken2("hello", "goodbye");
    void broken3(@error Unknown... p) {}
    @error broken3("hello");
    void broken4(@error Unknown[]... p) {}
    @error broken4({"hello"});
    void broken5<T>(@error Entry<T,Unknown> e) {}
    @error broken5("hello"->"goodbye");
    
    void broken6(void param(@error String paramOfParam="hello")) {}
    
    class Super() {
    	shared default void greet(String greeting) {}
    }
    
    class Sub() extends Super() {
    	shared actual void greet(@error String greeting="hello") {}
    }
    
    void method()(@error String name="gavin")(@error String... names) {}

}
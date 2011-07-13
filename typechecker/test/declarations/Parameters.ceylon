class Parameters() {
    void x1(@type["String"] String s) {}
    void x2(@type["String"] String s, @type["Natural"] Natural n) {}
    void x3(@type["Empty|Sequence<String>"] String[] s) {}
    void x4(@type["Nothing|String"] String? s) {}
    void x5(@type["Empty|Sequence<String>"] String... s) {}
    
    void d1(String name="World") {}
    void d2(Natural count=0) {}
    void d3(@error String name=0) {}
    void d4(@error Natural count="World") {}
    void d5(String? name=null) {}
    void d6(@error String? name="World") {}
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

}
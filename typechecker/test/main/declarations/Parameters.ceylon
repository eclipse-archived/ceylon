import ceylon.language { Any=Anything }

class Parameters() {
    void x1(@type:"String" String s) {}
    void x2(@type:"String" String s, @type:"Integer" Integer n) {}
    void x3(@type:"Sequential<String>" String[] s) {}
    void x4(@type:"Null|String" String? s) {}
    void x5(@type:"Sequential<String>" String* s) {}
    
    void x6(String s="hello", @error Integer n) {}
    void x7(String* s, @error Integer n) {}
    void x8(String* s, @error Integer n=0) {}

    void x9(@error String* s=[]) {}
    void x10(@type:"Integer" Integer f(Integer i)=>i) {}
    void x11(@error function f(Integer i)=>i) {}
    void x12(@error void f(Integer i)=>i) {}
    void x13(@error value s="hello") {}
    void x14(@error void f) {}
    void x15(void f(Integer i)=>print(i)) {}
    
    void d1(String name="World") {}
    void d2(Integer count=0) {}
    void d3(@error String name=0) {}
    void d4(@error Integer count="World") {}
    void d5(String? name=null) {}
    void d6(String? name="World") {}
    void d7(variable String s) {}
    void d8(@error variable String s()) {}
    void d9(variable String s="") {}
    
    void broken1(@error Unknown p) {}
    @error broken1("hello");
    void broken2<T>(T t, @error Unknown p) {}
    @error broken2("hello", "goodbye");
    void broken3(@error Unknown* p) {}
    @error broken3("hello");
    void broken4(@error Unknown[]* p) {}
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
    
    void method()(@error String name="gavin")(@error String* names) {}
    
    void func(String* x, @error String* y) {}
    
    void withDefaultedCallableParams(Float f() => 0.5, Float g(Float x) => x) {}
    void withDefaultedFunctionParams(Float() f = () => 0.5, Float(Float) g = (Float x) => x) {}
    
    @error print(String({Character*} chars1));
    @error value b1 = String({Character*} chars2);
    @error print(print(Any val1));
    @error value b2 = print(Any val2);
    
    void assigns1(
            variable Integer i, 
            @error Integer j=i++, 
            @error Integer k=(--i)*2, 
            @error Integer l=1+(i=0)) {}
    
    @error void assigns2(i, j=i++) {
        variable Integer i; 
        Integer j;
    }
    
    @error void refsforward(i, j=k) {
        variable Integer i; 
        Integer j;
        Integer k = 0;
    }

}
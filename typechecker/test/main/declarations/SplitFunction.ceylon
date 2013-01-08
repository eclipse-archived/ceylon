import ceylon.language { Anything }

void splitFunction() {
    Float f(Float x);
    Float g(Float x);
    Integer h(Float x);
    Float i(Float x);
    Float j(Object x);
    Float k(Float x);
    Float l(Float x);
    
    f(Float x) => x**0.5;
    j(Float x) => x**0.5;
    
    @error g(Float y, Float x) => x**0.5;
    h(@error Integer x) => x**2;
    @error i() => 0.5;
    k(@error Object x) => 1.0;
    @error l(Float x) => 3;
    
    Float add(Float x)(Float y);
    add(Float x)(Float y) => x+y;

    Float add1(Float x)(Integer y);
    add1(Float x) => (Integer y) => x+y.float;

    Float add2(Float x)(Integer y);
    add2(Float x)(Integer y) => x+y.float;

    Float add3(Integer x)(Float y);
    add3(@error Float x)(@error Integer y) => x+y.float;

    Float add4(Float x)(Integer y);
    @error add4(Float x) => x;

    Float add5(Float x)(Integer y);
    @error add5(Float x)(Integer y)(Float z) => x+z;
    
    abstract class Abstract() {
        shared formal Float add(Float x)(Float y);
        shared formal Float add0(Float x)(Float y);
    }
    
    class Concrete() extends Abstract() {
        add(Float x)(Float y) => x+y;
        add0(Float x)(@error Integer y) => x+y.float;
    }

    Anything func0(String[] x, String... y);
    func0(String[] x, String... y) => nothing;
    
    Anything func1(String[] x, String[] y);
    func1(String... x, @error String... y) => nothing;
    
    Anything func2(String... x, @error String... y);
    //func2(String[] x, String[] y) = nothing;
    
    Anything func3(String[] x, String[] y);
    func3(String[] x, @error String... y) => nothing;
    
    Anything func4(String[] x, String... y);
    func4(String[] x, @error String[] y) => nothing;
    
    Anything printit(String s);
    printit(@error String s="hello") => print(s);
    
    Anything higher1(void f(String s), String g(Integer i));
    higher1(void f(String s), String g(Integer i)) => print(f(g(0)));
    
    Anything higher2(void f(String s), String g(Integer i));
    higher2(void f(String s), @error String g(Float i)) => print(f(g(0.0)));
    
    Anything higher3(void f(String s), String g(Integer i));
    higher3(void f(String s), String g(Object i)) => print(f(g(0.0)));
    
    void myvoid();
    myvoid() => print("hello");
    void brokenvoid();
    @error brokenvoid() => "hello";
    
    void higher(Anything f(), void g()) { f(); g(); }
    higher { f()=>0; @error g()=>0; };
    //higher { @error f()=0; @error g()=0; };
    higher { function f()=>0; @error void g()=>0; };
    higher { function f()=>0; @error function g()=>0; };
    higher { function f()=>0; Integer g()=>0; };
}
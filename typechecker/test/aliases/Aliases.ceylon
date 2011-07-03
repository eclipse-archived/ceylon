class Aliases() {
    
    class C(String s) = Class<String>;
    @type["Class<String>"] C("hello");
    C("hello").hello("gavin");
    C c = C("hello");
    c.hello("gavin");
    Class<String> csp = c;
    Class<String> cs = Class<String>("gday");
    @type["Class<String>"] C cp = cs;
    @type["Class<String>"] value l = C("hi");
    
    class D() extends C("greetings") {}
    
    C cd = D();
    Class<String> csd = D();

    interface I<X> = Interface<X>;
    class B<X>(X n) satisfies I<X> {
        shared actual X name = n;
    }
    B<String> b = B("gavin");
    String n = b.name;
    @type["Interface<String>"] I<String> i = b;
    Interface<String> ins = i;
    function m() {
        return i;
    }
    @type["Interface<String>"] m();
    
    interface IS = Interface<String>;
    @type["Interface<String>"] IS isa = i;
    Interface<String> insa = isa;
    
}
void statements() {
    
    class X() {
        shared String hello = "hello";
        shared void doIt() {}
    }
    void y() {}
    X x { return x; }
    
    @error x();
    @error X().hello();
    @error z();
    @error X().z();
    
    X();
    y();
    X().doIt();
    { X() }[].doIt();
    X? xn = null;
    xn?.doIt();
    
    @error x;
    @error X().hello;
    @error x.hello;
    
    @error "Hello" + "World";
    @error "Hello"[0];
    @error { "Hello", "World" };
    @error { X() }[].hello;
    @error xn?.hello;
    
    @error true;
    
    abstract class Z(String z) {}
    
    @error Z("hello");
    @error Basic();
    @error Equality();
    @error process();
    
    @error throw 1;
        
}
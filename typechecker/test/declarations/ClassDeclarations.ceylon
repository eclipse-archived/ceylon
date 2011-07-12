abstract class ClassDeclarations() {
    
    void use(String s) {}
    
    shared formal String hello;
    shared formal void greet();
    void sayHello() {
        @error use(hello);
    }
    void performGreeting() {
        @error greet();
    }
    
    void foo() {
        @error bar();
    }
    
    void bar() {
        foo();
    }
    
    @error sayGoodbye();
    @error use(goodbye);
    
    @error use(hello);
    @error greet();
    sayHello();
    
    shared formal String goodbye;
    void sayGoodbye() {
        use(goodbye);
    }
    
    void baz() {
        qux();
    }
    
    void qux() {
        baz();
    }
    
    void z() { x(); }
    shared formal void x();
    void y() { x(); }
    
}
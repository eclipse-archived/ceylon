interface InterfaceDeclarations {
    
    formal String hello;
    void sayHello() {
        hello;
    }
    
    void foo() {
        bar();
    }
    
    void bar() {
        foo();
    }
    
    @error sayHello();
    
    formal String goodbye;
    void sayGoodbye() {
        goodbye;
    }
    
    void baz() {
        qux();
    }
    
    void qux() {
        baz();
    }
    
}
interface InterfaceDeclarations {
    
    void print(String s) {}
    
    shared formal String hello;
    void sayHello() {
        print(hello);
    }
    
    void foo() {
        bar();
    }
    
    void bar() {
        foo();
    }
    
    @error sayHello();
    
    shared formal String goodbye;
    void sayGoodbye() {
        print(goodbye);
    }
    
    void baz() {
        qux();
    }
    
    void qux() {
        baz();
    }
    
}
abstract class ClassDeclarations() {
    
    void use(String s) {}
    
    shared formal String hello;
    void sayHello() {
        @error use(hello);
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
    
}
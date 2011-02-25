abstract class ClassDeclarations() {
    
    shared formal String hello;
    void sayHello() {
        @error hello;
    }
    
    void foo() {
        @error bar();
    }
    
    void bar() {
        foo();
    }
    
    @error sayGoodbye();
    @error goodbye;
    
    @error hello;
    sayHello();
    
    shared formal String goodbye;
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
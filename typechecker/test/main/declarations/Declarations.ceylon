@error;
interface Declarations {
    
    class Y() {}
    class X(Y y) {}
    
    void doSomething() {}
    
    Y createSomething() { return Y(); }
    
    Y something { return createSomething(); }
    
    class WithValueArg(@error Object value) {}
    void withValueArg(@error Object value) {}
    
    //@error class value() {}
    //@error void object() {}
    //@error value function = "hello";
    
    @error class WithoutArgs {}
    @error void withoutArgs {}
    
    @error assign withoutGetter {}
    
    @error X methodWithoutReturn() {
        doSomething();
    }
    
    @error X getterWithoutReturn {
        doSomething();
    }
    
    @error class WithoutClassDef();
    @error interface WithoutInterfaceDef;
    @error object withoutValueDef;

    /*class Foo(String bar) {}
    @error Foo getFoo() {}
    @error Foo foo {}*/
    
    void duplicate() {}
    @error void duplicate(Y y) {}
    
    class Dupe() {}
    @error class Dupe(X x) {}
    
    void withDupeParam(X x, @error X x) {}
    void withoutDupeParam(X x(Y y), X y(Y y)) {}
    
    @error class () {}
    //@error void () {}
    @error interface {}
    //@error value { return Y(); }
    //@error function () { return Y(); }
    
    class XXX() {
        object x {
            String hello = "Hello";
        }
    }
    
    void a(String s = "a string") {}
    class A(Integer count = 1) {}
    
    void dup(String name, @error String name) {}
    
    void method() {
        String hello = "hi";
        for (c in hello.sequence) {
            if (c=='h') {
                try {
                    @error Integer hello = 1;
                }
                finally {}
            }
        }
        Integer? count = null;
        if (exists count) {}
    }
    
    class Outer() {
        String s = "hello";
        if (true) {
            @error print(s);
            @error String s = "goodbye";
        }
        for (x in {1, 2}) {
            @error print(s);
            @error String s = "goodbye";
        }
        class Inner() {
            @error print(s);
            String s = "hello again";
        }
        void member() {
            @error print(s);
            String s = "hello again";
        }
    }
    
    class SharedInference() {
    
        @error shared value sharedInferredSimple = "bad";
        @error shared value sharedInferredGetter { return "bad"; }
        @error shared function sharedInferredMethod() { return "bad"; }
        
    }
    
    shared String getterForSharedSetter {
        return "hello";
    }
    
    @error shared assign getterForSharedSetter {
    }
    
    class Niẍoℳᵫᵃ() {
        Niẍoℳᵫᵃ niẍoℳᵫᵃ = Niẍoℳᵫᵃ();
    }
    
    void _bar() { _bar(); }
    
    @error object obj {}
    
    void myvoid() => print("hello");
    @error void brokenvoid() => "hello";
    
    interface Constrained<T> 
        given T satisfies Object 
        @error given T satisfies Category {}
    
    void constrained<T>()
        given T satisfies Object 
        @error given T satisfies Category {}
}
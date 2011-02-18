interface Declarations {
    
    class Y() {}
    class X(Y y) {}
    
    void doSomething() {}
    
    Y createSomething() { return Y(); }
    
    Y something { return createSomething(); }
    
    @error class WithoutArgs {}
    
    @error void withoutArgs {}
    
    @error assign withoutGetter {}
    
    @error X methodWithoutReturn() {
        doSomething();
    }
    
    @error X getterWithoutReturn {
        doSomething();
    }
    
    void duplicate() {}
    @error void duplicate(Y y) {}
    
    class Dupe() {}
    @error class Dupe(X x) {}
    
    @error class () {}
    @error void () {}
    @error interface {}
    @error local { return Y(); }
    @error local () { return Y(); }
    
    object x {
        String hello = "Hello";
    }
    
    void a(String s = "a string") {}
    class A(Natural count = 1) {}
    
    void dup(String name, @error String name) {}

}
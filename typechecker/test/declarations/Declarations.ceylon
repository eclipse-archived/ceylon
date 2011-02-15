interface Declarations {
    
    class Y() {}
    class X(Y y) {}
    
    void doSomething() {}
    
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

}
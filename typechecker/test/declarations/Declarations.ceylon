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
    
    void withDupeParam(X x, @error X x) {}
    void withoutDupeParam(X x(Y y), X y(Y y)) {}
    
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
    
    class Hiding(String name) {
        shared String name = name;
    }
    
    class HidingWithTypeConstraint<T>(String name)
            given T(String name) {
        shared String name = name;
        shared T t = T(name);
    }
    
    class AdvancedHiding(Float x) {
        shared String x = x.string;
        @type["Float"] local f = x;
    }
    void advancedHiding() {
        @type["String"] local s = AdvancedHiding(1.0).x;
    }

}
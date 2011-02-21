class Generics() {

    class TypeWithParameter<X>(X init) {
        shared variable X x := init;
        shared X process(X input) { return input; }
    }
    
    class TypeWithMultipleParameters<X,Y>(Map<X,Y> map) {
        shared Y y(X x) { return map[y]; }
    }
    
    class TypeWithInParameter<in X>(String toString(X x)) {
        shared String consume(X x) { return toString(x); }
    }
    
    class TypeWithOutParameter<out X>(X x) {
        shared X produce() { return x; }
        shared X x = x;
    }
    
    interface TypeWithVariantParameters<out X, in Y, Z> {
        shared formal void consume(Y y);
        shared formal X produce();
        shared formal variable Z z;
    }
    
    class TypeWithUpperBoundParameter<X>(X produce())
            given X satisfies String {
        String s = produce();
    }

    class TypeWithLowerBoundParameter<X>(void accept(X x), X xx)
            given X abstracts String {
        accept(xx);
    }
        
    class TypeWithConstructableParameter<X>(String s, Natural n)
            given X(String s, Natural n) {
        X x = X(s,n);
    }

    class TypeWithMultipleParameterConstraints<X>(String s, Natural n)
            given X(String s, Natural n) satisfies String {
        String sn = X(s,n);
    }


    X methodWithParameter<X>(X x) { return x; }
    
    Entry<X,Y> methodWithMultipleParameters<X,Y>(X x, Y y) { return Entry(x, y); }
    
    String methodWithUpperBoundParameter<X>(X produce())
        given X satisfies String { return produce(); }

    void methodWithLowerBoundParameter<X>(void accept(X x), X xx)
        given X abstracts String { accept(xx); }
        
    X methodWithConstructableParameter<X>(String s, Natural n)
        given X(String s, Natural n) { return X(s,n); }

    String methodWithMultipleParameterConstraints<X>(String s, Natural n)
        given X(String s, Natural n) satisfies String { return X(s,n); }


    interface Processor<out X, in Y> {
        shared formal X process(Y y);
    }
    
    class ProcessorImpl<out X, in Y>(X p(Y y)) satisfies Processor<X,Y> {
        shared actual X process(Y y) {
            return p(y);
        }
    }
    
    String stringify<Y>(Y y) given Y satisfies Number { return $y; }
    String zero = stringify<Natural>(0);

    Processor<String, Natural> p = ProcessorImpl<String, Natural>(stringify);
    String one = p.process(1);
    
    void output<Y>(Y value, Processor<String,Y> p) 
            given Y satisfies Number {
        log.info(p.process(value));
    }
    
    output(2,p);
    
    /*class ClassWithDimensionalParameter<#n>() {
        shared variable Bounded<#n> count := 0;
    }
    
    interface TypeWithDimensionalParameters<#m,#n> {
        shared formal Float<#m,#n> matrix;
    }*/
    
    class ClassWithSequencedTypeParameter<P...>(Callable<Void,P...> callable) {
        T call(P... args) {
            callable(args);
        }
    }
    
    interface InterfaceWithSequencedTypeParameter<T,Q...> {
        shared formal T invokeSomething(Q... params);
    }

}

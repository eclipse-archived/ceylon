class GenericRefinement() {
    
    interface GoodTypes {
        
        class Foo<T,S>() {
            shared default T foo(T t) { return t; }
            shared default S bar(S s) { return s; }
            shared default T t { throw; }
            shared default S s { throw; }
        }
        
        class Bar<Q>() extends Foo<Q,String>() {
            shared actual Q foo(Q t) { return t; }
            shared actual String bar(String s) { return s; }
            shared actual Q t { throw; }
            shared actual String s { throw; }
        }
        
    }
    
    interface NestedTypes {
        
        interface Foo<X> {
            shared formal X x();
        }
        
        interface Bar<X> {
            shared formal Foo<X> foo();
        }
        
        class BarImpl1<Y>(Y x) satisfies Bar<Y> {
            shared actual Foo<Y> foo() {
                class FooImpl() satisfies Foo<Y> {
                    shared actual Y x() { return x; }
                }
                return FooImpl();
            }
        }
        class BarImpl2<Y>(Y x) satisfies Bar<Y> {
            shared actual Foo<Y> foo() {
                object foo satisfies Foo<Y> {
                    shared actual Y x() { return x; }
                }
                return foo;
            }
        }
        
    }

    interface NestedTypes2 {
        
        interface Foo<X> {
            shared formal X x();
        }
        
        interface Bar<X> {
            shared formal Foo<X> foo();
        }
        
        class BarImpl1<X>(X x) satisfies Bar<X> {
            shared actual Foo<X> foo() {
                class FooImpl() satisfies Foo<X> {
                    shared actual X x() { return x; }
                }
                return FooImpl();
            }
        }
        class BarImpl2<X>(X x) satisfies Bar<X> {
            shared actual Foo<X> foo() {
                object foo satisfies Foo<X> {
                    shared actual X x() { return x; }
                }
                return foo;
            }
        }
        
    }

}
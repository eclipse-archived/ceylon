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

}
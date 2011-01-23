doc "Represents an empty sequence."
shared object none {

    shared extension class EmptySequence<out X>() 
            satisfies X[0] 
            given X satisfies Equality<X> {
        
        shared actual Natural? lastIndex = null;
        
        shared actual Gettable<X?> value(Natural index) {
            X? nullValue = null;
            return nullValue 
        }
        
        shared actual Gettable<X> value(Bounded<#0> index) {
            throw Exception()
        }
        
    }
    
}
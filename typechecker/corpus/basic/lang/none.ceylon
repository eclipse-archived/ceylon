doc "Represents an empty sequence."
shared object none {

    shared extension class EmptySequence<out X>() 
            satisfies X[]
            given X satisfies Equality<X> {
        
        shared actual Natural? lastIndex = null;
        
        shared actual Gettable<X?> value(Natural index) {
            X? nullValue = null;
            return nullValue;
        }
        
    }
    
}
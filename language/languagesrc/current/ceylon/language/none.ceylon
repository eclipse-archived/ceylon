doc "Represents an empty sequence."
object empty satisfies Bottom[] {
    
    shared actual Natural? lastIndex = null;
    
    shared actual Nothing value(Natural index) {
        return null;
    }        
    
}

doc "Hides the concrete type of empty."  
shared Bottom[] none = empty;
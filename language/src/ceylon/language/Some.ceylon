shared interface Some<out Element> 
        satisfies FixedSized<Element> {
    
    shared actual default Element first {
        if (is Element first = iterator.next()) {
            return first;
        }
        else {
            throw;
        }
    }
    
    doc "Returns `false`, since every `Some` contains at 
         least one element."
    shared actual Boolean empty { 
        return false; 
    }

    shared formal FixedSized<Element> rest;
    
}
object emptyIterator satisfies Iterator<Bottom> {
    shared actual Finished next() {
        return exhausted;
    }
}

shared interface None<out Element> 
        satisfies FixedSized<Element> {
    
    shared actual Nothing first {
        return null;
    }
    
    shared actual default Iterator<Element> iterator {
        return emptyIterator;
    }
    
    shared actual default Integer size { 
        return 0; 
    }
    
    shared actual Boolean empty { 
        return true; 
    }
    
}

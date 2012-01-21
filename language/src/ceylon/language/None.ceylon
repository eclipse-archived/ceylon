shared interface None<out Element> 
        satisfies FixedSized<Element> {
    
    shared actual Nothing first {
        return null;
    }
    
    shared actual Iterator<Bottom> iterator {
        object emptyIterator satisfies Iterator<Bottom> {
            shared actual Finished next() {
                return exhausted;
            }
        }
        return emptyIterator;
    }
    
    shared actual Integer size { return 0; }
    shared actual Boolean empty { return true; }
}
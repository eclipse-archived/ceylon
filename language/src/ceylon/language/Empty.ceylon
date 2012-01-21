doc "A sequence with no elements. The type of the expression
     `{}`."
see (Sequence)
shared interface Empty
           satisfies List<Bottom> & None<Bottom> {
    
    shared actual Integer size { 
        return 0; 
    }
    shared actual Boolean empty { 
        return true; 
    }
    shared actual Iterator<Bottom> iterator {
        object emptyIterator satisfies Iterator<Bottom> {
            shared actual Finished next() {
                return exhausted;
            }
        }
        return emptyIterator;
    }
    shared actual Nothing item(Integer key) {
        return null;
    }
    
    /*shared actual Empty segment(Integer from, Integer length) {
        return this;
    }
    
    shared actual Empty span(Integer from, Integer? to) {
        return this;
    }*/
    
    shared actual String string {
        return "{}";
    }
    
}
doc "A sequence with no elements. The type of the expression
     `{}`."
see (Sequence)
shared interface Empty
           satisfies List<Bottom> & None<Bottom> &
                     Ranged<Integer,Empty> {
    
    shared actual Integer size { return 0; }
    
    shared actual Iterator<Bottom> iterator {
        return emptyIterator;
    }
    
    shared actual Nothing item(Integer key) {
        return null;
    }
    
    shared actual Empty segment(Integer from, Integer length) {
        return this;
    }
    
    shared actual Empty span(Integer from, Integer? to) {
        return this;
    }
    
    shared actual String string {
        return "{}";
    }
    shared actual Integer? lastIndex { return null; }
    
    shared actual Empty rest { return this; }
    
}
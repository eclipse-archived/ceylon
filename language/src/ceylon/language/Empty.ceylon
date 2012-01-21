doc "A sequence with no elements. The type of the expression
     `{}`."
see (Sequence)
shared interface Empty
           satisfies List<Bottom> & None<Bottom> {
    
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
    
}
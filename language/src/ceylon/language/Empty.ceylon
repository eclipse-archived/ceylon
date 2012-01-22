doc "A sequence with no elements. The type of the expression
     `{}`."
see (Sequence)
shared interface Empty
           satisfies List<Bottom> & None<Bottom> &
                     Ranged<Integer,Empty> &
                     Cloneable<Empty> {
    
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
    shared actual Nothing lastIndex { return null; }
    
    //shared actual Empty rest { return this; }
    
    shared actual Empty clone {
        return this;
    }
    
    shared actual Boolean contains(Equality element) {
        return false;
    }
    
    shared actual Integer count(Equality element) {
        return 0;
    }
    
    shared actual Boolean defines(Integer index) {
        return false;
    }
    
}
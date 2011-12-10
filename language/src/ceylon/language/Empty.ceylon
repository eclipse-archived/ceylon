shared interface Empty
           satisfies Correspondence<Integer, Bottom> & 
                     Sized & Ordered<Bottom> & 
                     Ranged<Integer,Empty> {
    
    shared actual Integer size { 
        return 0; 
    }
    shared actual Boolean empty { 
        return true; 
    }
    shared actual Nothing iterator {
        return null;
    }
    shared actual Nothing item(Integer key) {
        return null;
    }
    shared actual Nothing first {
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
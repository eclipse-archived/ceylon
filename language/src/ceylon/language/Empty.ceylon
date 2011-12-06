shared interface Empty
           satisfies Correspondence<Natural, Bottom> & 
                     Sized & Ordered<Bottom> & 
                     Ranged<Natural,Empty> {
    
    shared actual Natural size { 
        return 0; 
    }
    shared actual Boolean empty { 
        return true; 
    }
    shared actual Nothing iterator {
        return null;
    }
    shared actual Nothing item(Natural key) {
        return null;
    }
    shared actual Nothing first {
        return null;
    }
    
    shared actual Empty segment(Natural from, Natural length) {
        return this;
    }
    
    shared actual Empty span(Natural from, Natural? to) {
        return this;
    }
    
    shared actual String string {
        return "{}";
    }
    
}
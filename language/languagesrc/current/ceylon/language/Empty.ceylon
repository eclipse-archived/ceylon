shared interface Empty
           satisfies Correspondence<Natural, Bottom> & 
                     Sized & Ordered<Bottom> {
    
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
    
}
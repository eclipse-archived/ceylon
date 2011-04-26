object emptyIterator satisfies Iterator<Bottom> {
    
    shared actual Nothing head { 
        return null; 
    }
    shared actual Iterator<Bottom> tail { 
        return this; 
    }
    
}

shared interface Empty
           satisfies Correspondence<Natural, Bottom> & 
                     Iterable<Bottom> & Sized {
    
    shared actual Natural size { 
        return 0; 
    }
    shared actual Boolean empty { 
        return true; 
    }
    shared actual Iterator<Bottom> iterator() {
        return emptyIterator;
    }
    shared actual Nothing value(Natural key) {
        return null;
    }
    
}
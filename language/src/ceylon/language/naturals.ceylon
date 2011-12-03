doc "An iterator that produces the natural numbers, starting
     from 0."
by "Gavin"
shared object naturals 
        satisfies Ordered<Natural> & Ranged<Natural,Natural[]> {
    
    class NaturalIterator(Natural from) 
            satisfies Iterator<Natural> {
        shared actual Natural head { 
            return from; 
        }
        shared actual Iterator<Natural> tail {
            return NaturalIterator(from+1);
        }
    }
    
    shared actual Iterator<Natural> iterator = NaturalIterator(0);

    shared actual Range<Natural> span(Natural from, Natural to) {
        return from..to;
    }
    
    shared actual Natural[] segment(Natural from, Natural length) {
        if (length==0) {
            return {};
        }
        else {
            return from..from+length-1;
        }
    }

}
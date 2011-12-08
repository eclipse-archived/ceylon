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

    shared actual Range<Natural> span(Natural from, Natural? to) {
        if (exists to) {
            return from..to;
        }
        else {
            throw; //TODO!
        }
    }
    
    shared actual Natural[] segment(Natural from, Natural length) {
        return length==0 then {} 
                else from..from+length.predecessor;
    }

}
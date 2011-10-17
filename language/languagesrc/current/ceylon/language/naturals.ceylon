doc "An iterator that produces the natural numbers, starting
     from 0."
by "Gavin"
shared object naturals satisfies Ordered<Natural> {
    
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

}
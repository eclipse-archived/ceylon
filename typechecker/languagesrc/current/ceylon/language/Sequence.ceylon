shared interface Sequence<out Element> 
        //is EnumerableSequence<Element>
        satisfies Correspondence<Natural, Element> & Iterable<Element> & 
                  Sized & Cloneable<Sequence<Element>> {
    
    doc "The index of the last element of the sequence."
    shared formal Natural lastIndex;
    
    doc "The first element of the sequence."
    shared actual formal Element first;
    
    doc "The rest of the sequence, without the first
         element."
    shared formal Element[] rest;
    
    shared actual Boolean empty {
        return false;
    }
    
    shared actual default Natural size {
        return lastIndex+1;
    }
    
    doc "The last element of the sequence."
    shared default Element last {
        if (exists Element x = element(lastIndex)) {
            return x;
        }
        else {
            return first; //actually never occurs
        } 
    }
    
    shared actual default Boolean defines(Natural index) {
        return index<=lastIndex;
    }
    
    //this depends on efficient implementation of rest
    /*
    shared actual default object iterator 
            extends Object()
            satisfies Iterator<Element> {
        shared actual Element head { 
            return first;
        }
        shared actual Iterator<Element> tail {
            return rest.iterator;
        }
    }
    */
    
    shared actual default Iterator<Element> iterator {
        return SequenceIterator(0);
    }
    
    class SequenceIterator(Natural from)
            extends Object()
            satisfies Iterator<Element> {
        shared actual Element? head { 
            return element(from);
        }
        shared actual Iterator<Element> tail {
            return SequenceIterator(from+1);
        }
    }
    
}
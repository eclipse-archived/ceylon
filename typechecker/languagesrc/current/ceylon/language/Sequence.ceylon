shared interface Sequence<out X> 
        //is EnumerableSequence<X>
        satisfies Correspondence<Natural, X> & Iterable<X> & 
                  Sized & Cloneable<Sequence<X>> {
    
    doc "The index of the last element of the sequence."
    shared formal Natural lastIndex;
    
    doc "The first element of the sequence."
    shared formal X first;
    
    doc "The rest of the sequence, without the first
         element."
    shared formal X[] rest;

    shared actual Boolean empty {
        return false;
    }
        
    shared actual default Natural size {
        return lastIndex+1;
    }
    
    doc "The last element of the sequence."
    shared default X last {
        if (exists X x = value(lastIndex)) {
            return x;
        }
        else {
            return first; //actually never occurs
        } 
    }

    shared actual default Iterator<X> iterator() {
        class SequenceIterator(Natural from) 
                satisfies Iterator<X> {
            shared actual X? head { 
                return value(from);
            }
            shared actual Iterator<X> tail {
                return SequenceIterator(from+1);
            }
        }
        return SequenceIterator(0);
    }
    
}
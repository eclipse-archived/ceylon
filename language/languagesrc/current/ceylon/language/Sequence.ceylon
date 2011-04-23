shared interface Sequence<out X> 
        //is EnumerableSequence<X>
        satisfies Correspondence<Natural, X> & Iterable<X> & Sized & Cloneable<X[]> {

    doc "The index of the last element of the sequence,
         or |null| if the sequence has no elements."
    shared formal Natural? lastIndex;
    
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
    
    shared actual default Natural size {
        if (exists lastIndex) {
            return lastIndex+1;
        }
        else {
            return 0;
        } 
    }
    
    shared actual default Boolean empty {
        return !(this.lastIndex exists);
    }
        
    doc "The first element of the sequence, or
         |null| if the sequence has no elements."
    shared default X? first {
        return this[0];
    }
    
    doc "The last element of the sequence, or
         |null| if the sequence has no elements."
    shared default X? last {
        if (exists lastIndex) {
            return this[lastIndex];
        }
        else {
            return null;
        } 
    }

    doc "The rest of the sequence, after removing 
         the first element."
    shared default X[] rest {
        return this[1...];
    }
    
}
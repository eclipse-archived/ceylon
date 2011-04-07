shared interface Sequence<out X> 
        is EnumerableSequence<X>
        satisfies Correspondence<Natural, X> & FiniteCategory<X> & Sized
        given X satisfies Equality<X> {

    doc "The index of the last element of the sequence,
         or |null| if the sequence has no elements."
    shared formal Natural? lastIndex;
    
    shared actual default Iterator<X> iterator() {
        class SequenceIterator(Natural from) 
                satisfies Iterator<X> {
            shared actual X? head { 
                return this[from];
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
    
    shared actual default Boolean contains(Object obj) {
        if (is X obj) {
            return forAny (X x in this) some (x==obj);
        }
        else {
            return false;
        }
    }
    
    doc "The first element of the sequence, or
         |null| if the sequence has no elements."
    shared default X? first {
        return this[0];
    }
    
    doc "The rest of the sequence, after removing 
         the first element."
    shared default X[] rest {
        return this[1...];
    }

    doc "The last element of the sequence, or
         |null| if the sequence has no elements."
    shared default X? last {
        if (exists Natural index = sequence.lastIndex) {
            return this[index];
        }
        else {
            return null;
        }
    }
   
}
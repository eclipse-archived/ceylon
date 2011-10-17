doc "A nonempty sequence of values. Sequence does not 
     satisfy Category, simply because the contains() 
     operation may be inefficient for some sequences."
by "Gavin"
see (List)
shared interface Sequence<out Element> 
        satisfies Correspondence<Natural, Element> &  
                  Ordered<Element> & Sized &
                  Cloneable<Sequence<Element>> {
    
    doc "The index of the last element of the sequence."
    see (size)
    shared formal Natural lastIndex;
    
    doc "The first element of the sequence, that is, the
         element with index 0."
    shared actual formal Element first;
    
    doc "The rest of the sequence, without the first 
         element."
    shared formal Element[] rest;
    
    doc "Returns false, since every Sequence contains at 
         least one element."
    shared actual Boolean empty {
        return false;
    }
    
    doc "The number of elements in this sequence, always
         sequence.lastIndex+1."
    see (lastIndex)
    shared actual default Natural size {
        return lastIndex+1;
    }
    
    doc "The last element of the sequence."
    shared default Element last {
        if (exists Element x = item(lastIndex)) {
            return x;
        }
        else {
            return first; //actually never occurs
        } 
    }
    
    doc "Determines if the given index refers to an element
         of this sequence, that is, if 
         index<=sequence.lastIndex."
    shared actual default Boolean defines(Natural index) {
        return index<=lastIndex;
    }
    
    doc "Returns the element of this sequence with the given
         index, or null if the given index is past the end
         of the sequence, that is, if 
         index>sequence.lastIndex. The first element of the
         sequence has index 0."
    shared actual formal Element? item(Natural index);
    
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
            return item(from);
        }
        shared actual Iterator<Element> tail {
            return SequenceIterator(from+1);
        }
        shared actual String string {
            return "SequenceIterator";
        }
    }
    
    doc "Select the elements between the given indexes.
         If the start index is larger than the end index, or 
         larger than the last index of the string, return 
         an Empty sequence. Otherwise, if the end index is 
         larger than the last index of the string, return 
         all elements from the start index to the end of 
         the string."
    //todo: would be better to support reverse spans?
    shared formal Element[] span(Natural from, Natural to);
    
    doc "Returns a sequence containing the elements 
         beginning from the given index, with the given
         length."
    shared actual formal Element[] segment(Natural from, 
                                           Natural length);
    
    /*shared formal Sequence<Value> append<Value>(Value... elements)
            given Value abstracts Element;
    
    shared formal Sequence<Value> prepend<Value>(Value... elements)
            given Value abstracts Element;*/
    
    shared default actual String string {
        return "{ " elementsString " }";
    }
    
    String elementsString {

        String firstString {
            if (is Object first) {
                return first.string;
            }
            else if (is Nothing first) {
                return "null";
            }
            else {
                throw;
            }
        }
        
        if (nonempty rest) {
            return firstString + ", " + 
                    rest.elementsString;
        }
        else {
            return firstString;
        }
        
    }
}
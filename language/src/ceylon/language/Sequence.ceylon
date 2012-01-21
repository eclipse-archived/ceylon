doc "A nonempty sequence of values. `Sequence` does not 
     satisfy `Category`, simply because the `contains()` 
     operation may be inefficient for some sequences."
by "Gavin"
shared interface Sequence<out Element> 
        satisfies List<Element> & Some<Element> &
                  Cloneable<Sequence<Element>> &
                  Ranged<Integer, Element[]> {
    
    doc "The index of the last element of the sequence."
    see (size)
    shared formal Integer lastIndex;
    
    doc "The first element of the sequence, that is, the
         element with index `0`."
    shared actual formal Element first;
    
    doc "The rest of the sequence, without the first 
         element."
    shared formal Element[] rest;
        
    doc "The number of elements in this sequence, always
         `sequence.lastIndex+1`."
    see (lastIndex)
    shared actual default Integer size {
        return lastIndex+1;
    }
    
    doc "The last element of the sequence, that is, the
         element with index `sequence.lastIndex`."
    shared default Element last {
        if (is Element last = item(lastIndex)) {
            return last;
        }
        else {
            throw; //actually never occurs
        } 
    }
    
    doc "Determines if the given index refers to an element
         of this sequence, that is, if 
         `index<=sequence.lastIndex`."
    shared actual default Boolean defines(Integer index) {
        return index<=lastIndex;
    }
    
    doc "Returns the element of this sequence with the given
         index, or `null` if the given index is past the end
         of the sequence, that is, if 
         `index>sequence.lastIndex`. The first element of 
         the sequence has index `0`."
    shared actual formal Element? item(Integer index);
    
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
        class SequenceIterator(Integer from)
                extends Object()
                satisfies Iterator<Element> {
            variable Integer idx := from;
            shared actual Element|Finished next() { 
                if (idx < lastIndex) {
                    return item(idx) ? exhausted;
                } else {
                    return exhausted;
                }
            }
            shared actual String string {
                return "SequenceIterator";
            }
        }
        return SequenceIterator(0);
    }
    
    /*doc "Reverse this sequence, returning a new nonempty 
         sequence."
    shared formal Sequence<Element> reversed;*/
    
    doc "Select the elements between the given indexes.
         If the start index is the same as the end index,
         return a sequence with a single element. If the 
         start index larger than the end index, return the 
         elements in the reverse order from the order in 
         which they appear in this sequence. If both the 
         start index and the end index are larger than the 
         last index in the sequence, return an `Empty` 
         sequence. Otherwise, if the last index is larger 
         than the last index in the sequence, return all 
         elements from the start index to last element of 
         the sequence."
    shared actual formal Element[] span(Integer from, 
                                        Integer? to);
    
    doc "Returns a sequence containing the elements 
         beginning from the given index, with the given
         length."
    shared actual formal Element[] segment(Integer from, 
                                           Integer length);
    
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

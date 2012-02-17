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
    shared actual formal Integer lastIndex;
    
    doc "The first element of the sequence, that is, the
         element with index `0`."
    shared actual formal Element first;
    
    doc "The last element of the sequence, that is, the
         element with index `sequence.lastIndex`."
    shared default Element last {
        if (is Element last = this[lastIndex]) {
            return last;
        }
        else {
            throw; //never occurs for well-behaved implementations
        } 
    }
    
    doc "The rest of the sequence, without the first 
         element."
    shared actual formal Element[] rest;
        
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
            else {
                return "null";
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

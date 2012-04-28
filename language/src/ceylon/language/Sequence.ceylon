doc "A nonempty sequence of values."
by "Gavin"
shared interface Sequence<out Element>
        satisfies List<Element> & Some<Element> &
                  Ranged<Integer,Element[]> &
                  Cloneable<Sequence<Element>> {
    
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
        
    /*shared actual formal Element[] span(Integer from,
                                        Integer? to);
                                        
    shared actual formal Element[] segment(Integer from,
                                           Integer length);*/
                                           
    /*shared formal Sequence<Value> append<Value>(Value... elements)
            given Value abstracts Element;
    
    shared formal Sequence<Value> prepend<Value>(Value... elements)
            given Value abstracts Element;*/
    
}

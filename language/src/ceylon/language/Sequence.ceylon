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
    shared actual default Element last {
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
        
    doc "Reverse this sequence, returning a new nonempty
         sequence."
    shared actual formal Sequence<Element> reversed;
    
    doc "This sequence."
    shared actual Sequence<Element> sequence {
        return this;
    }
    
    /*shared actual formal Element[] span(Integer from,
                                        Integer? to);
                                        
    shared actual formal Element[] segment(Integer from,
                                           Integer length);*/
                                           
    /*shared formal Sequence<Value> append<Value>(Value... elements)
            given Value abstracts Element;
    
    shared formal Sequence<Value> prepend<Value>(Value... elements)
            given Value abstracts Element;*/

    doc "Returns a new `List` that starts with the specified
         elements, followed by the elements of this `List`."
    shared actual default Sequence<Element|Other> withLeading<Other>(Other... others) {
        value sb = SequenceBuilder<Element|Other>();
        sb.appendAll(others...);
        sb.appendAll(this...);
        if (nonempty seq=sb.sequence) {
            return seq;
        }
        throw; //should never happen in a well-behaved implementation
    }

    doc "Returns a new `List` that contains the specified
         elements appended to the end of this `List`s'
         elements."
    shared actual default Sequence<Element|Other> withTrailing<Other>(Other... others) {
        value sb = SequenceBuilder<Element|Other>();
        sb.appendAll(this...);
        sb.appendAll(others...);
        if (nonempty seq=sb.sequence) {
            return seq;
        }
        throw; //should never happen in a well-behaved implementation
    }

    doc "Returns a sequence containing the elements of this
         container, sorted according to a function 
         imposing a partial order upon the elements."
    shared actual Seqeuence<Element> sort(
            doc "The function comparing pairs of elements."
            Comparison? comparing(Element x, Element y)) { throw; }

    doc "An eager version of `map`."
    shared actual Sequence<Result> collect<Result>(
            doc "The transformation applied to the elements."
            Result collecting(Element element)) {
        value s = map(collecting).sequence;
        if (nonempty s) {
            return s;
        }
        throw; //Should never happen in a well-behaved implementation
    }

}

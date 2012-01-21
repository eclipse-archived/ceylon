doc "Produces a `Ordered` iterator of each index to element 
     `Entry` for the given sequence of values."
shared Entry<Integer,Element>[] entries<Element>(Element... sequence) 
        given Element satisfies Equality {
    
    if (nonempty sequence) {
        
        object sequenceEntries
                extends Object()
                satisfies Sequence<Integer->Element> {
            
            shared actual Sequence<Integer->Element> clone { 
                return this;
            }
            
            shared actual String string { 
                return "sequenceEntries";
            }
            
            shared actual Integer lastIndex { 
                return sequence.lastIndex;
            }
            
            shared actual Integer->Element first {
                return 0->sequence.first;
            }

            shared actual Entry<Integer,Element>[] rest { 
                return entries(sequence.rest...);
            }

            shared actual Entry<Integer,Element>? item(Integer index) {
                if (exists element = sequence[index]) {
                    return index->element;    
                }
                else {
                    return null;
                }
            }

            shared actual Entry<Integer,Element>[] span(Integer from, Integer? to) {
                return entries(sequence.span(from, to)...);
            }

            shared actual Entry<Integer,Element>[] segment(Integer from, Integer length) {
                return entries(sequence.segment(from, length)...);
            }
            
            shared actual Iterator<Integer->Element> iterator {
                class EntryIterator(Integer from) 
                        extends Object()
                        satisfies Iterator<Integer->Element> {
                    variable Integer idx := from;
                    shared actual Entry<Integer,Element>|Finished next() {
                        if (exists Element item = sequence[idx]) {
                            return idx++->item;
                        }
                        else {
                            return exhausted;
                        }
                    }
                    shared actual String string {
                        return "EntryIterator";
                    }
                }
                return EntryIterator(0);
            }
                        
            shared actual Boolean equals(Equality that) {
                //TODO!
                return bottom;
            }
            
            shared actual Integer hash {
                return sequence.hash;
            }
            
            /*shared actual Element? item(Integer index) {
                return sequence[index];
            }
            
            shared actual Integer size {
                return sequence.size;
            }
            
            shared actual Map<Integer,Element> clone {
                if (nonempty sequence) {
                    return entries<Element>(sequence.clone...);
                }
                else {
                    return entries<Element>();
                }
            }*/
        }
        
        return sequenceEntries;
    
    }
    else {
        return {};
    }
    
}

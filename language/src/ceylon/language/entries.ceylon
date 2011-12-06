doc "Produces a `Ordered` iterator of each index to element 
     `Entry` for the given sequence of values."
shared Entry<Natural,Element>[] entries<Element>(Element... sequence) 
        given Element satisfies Equality {
    
    if (nonempty sequence) {
        
        object sequenceEntries
                extends Object()
                satisfies Sequence<Natural->Element> {
            
            shared actual Sequence<Natural->Element> clone { 
                return this;
            }
            
            shared actual String string { 
                return "sequenceEntries";
            }
            
            shared actual Natural lastIndex { 
                return sequence.lastIndex;
            }
            
            shared actual Natural->Element first {
                return 0->sequence.first;
            }

            shared actual Entry<Natural,Element>[] rest { 
                return entries(sequence.rest...);
            }

            shared actual Entry<Natural,Element>? item(Natural index) {
                if (exists element = sequence[index]) {
                    return index->element;    
                }
                else {
                    return null;
                }
            }

            shared actual Entry<Natural,Element>[] span(Natural from, Natural? to) {
                return entries(sequence.span(from, to)...);
            }

            shared actual Entry<Natural,Element>[] segment(Natural from, Natural length) {
                return entries(sequence.segment(from, length)...);
            }
            
            shared actual Iterator<Natural->Element> iterator {
                class EntryIterator(Natural from) 
                        extends Object()
                        satisfies Iterator<Natural->Element> {
                    shared actual Entry<Natural,Element> head {
                        if (exists Element item = sequence[from]) {
                            return from->item;
                        }
                        else {
                            throw;
                        }
                    }
                    shared actual Iterator<Natural->Element>? tail {
                        if (from<sequence.lastIndex) {
                            return EntryIterator(from+1);
                        }
                        else {
                            return null;
                        }
                    }
                    shared actual String string {
                        return "" sequence.string " from " from "";
                    }
                }
                return EntryIterator(0);
            }
            
            /*shared actual Element? item(Natural index) {
                return sequence[index];
            }
            
            shared actual Natural size {
                return sequence.size;
            }
            
            shared actual Map<Natural,Element> clone {
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

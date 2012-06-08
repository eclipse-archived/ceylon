doc "An alias for `Integer->Element`."
shared class Indexed<Element>(Integer index, Element element)
        given Element satisfies Object 
        = Entry<Integer,Element>;

doc "Produces a sequence of each index to element `Entry` 
     for the given sequence of values."
shared Indexed<Element>[] entries<Element>(Element... elements) 
        given Element satisfies Object {
    
    if (nonempty elems = elements.sequence) {
        
        object sequenceEntries
                extends Object()
                satisfies Sequence<Indexed<Element>> {
            
            shared actual Sequence<Indexed<Element>> clone { 
                return this;
            }
            
            shared actual String string { 
                return "sequenceEntries";
            }
            
            shared actual Integer lastIndex { 
                return elems.lastIndex;
            }
            
            shared actual Indexed<Element> first {
                return 0->elems.first;
            }

            shared actual Indexed<Element>[] rest { 
                return entries(elems.rest...);
            }

            shared actual Indexed<Element>? item(Integer index) {
                if (exists element = elems[index]) {
                    return index->element;    
                }
                else {
                    return null;
                }
            }

            shared actual Indexed<Element>[] span(Integer from, Integer? to) {
                return entries(elems.span(from, to)...);
            }

            shared actual Indexed<Element>[] segment(Integer from, Integer length) {
                return entries(elems.segment(from, length)...);
            }
            
            shared actual Iterator<Indexed<Element>> iterator {
                class EntryIterator(Integer from)
                        satisfies Iterator<Indexed<Element>> {
                    variable Integer idx := from;
                    shared actual Indexed<Element>|Finished next() {
                        if (exists Element item = elems[idx]) {
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
            
            shared actual Integer size {
                return elems.size;
            }
            
        }
        
        return sequenceEntries;
    
    }
    else {
        return {};
    }
    
}

doc "Produces a Map of index to element for the given 
     sequence of values."
shared Map<Natural,Element> entries<Element>(Element... sequence) 
        given Element satisfies Equality {
    
    object sequenceEntries
            extends Object()
            satisfies Map<Natural,Element> {
        
        shared actual Iterator<Entry<Natural,Element>> iterator {
            class EntryIterator(Natural from) 
                    extends Object()
                    satisfies Iterator<Entry<Natural,Element>> {
                shared actual Entry<Natural,Element>? head {
                    if (exists Element x = sequence[from]) {
                        return from->x;
                    }
                    else {
                        return null;
                    }
                }
                shared actual Iterator<Entry<Natural,Element>> tail {
                    return EntryIterator(from+1);
                }
                shared actual String string {
                    return "" sequence.string " from " from "";
                }
            }
            return EntryIterator(0);
        }
        
        shared actual Element? item(Natural index) {
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
        }
        
    }
    
    return sequenceEntries;
    
}

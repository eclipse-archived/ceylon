shared interface Entries<Key, out Element> 
        //is EnumerableEntries<Key,Element>
        satisfies Correspondence<Key, Element> & Iterable<Entry<Key,Element>> & 
                  Sized & Cloneable<Entries<Key,Element>> 
        given Key satisfies Equality
        given Element satisfies Equality {}

shared Entries<Natural,Element> entries<Element>(Element... sequence) 
        given Element satisfies Equality {
    
    object sequenceEntries
            extends Object()
            satisfies Entries<Natural,Element> {
        
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
            }
            return EntryIterator(0);
        }
        
        shared actual Element? element(Natural index) {
            return sequence[index];
        }
        
        shared actual Natural size {
            return sequence.size;
        }
        
        shared actual Entries<Natural,Element> clone {
            if (nonempty sequence) {
                return entries<Element>(sequence.clone);
            }
            else {
                return entries<Element>({});
            }
        }
        
    }
    
    return sequenceEntries;
    
}

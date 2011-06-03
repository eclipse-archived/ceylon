shared interface Entries<Key, out Value> 
        //is EnumerableEntries<Key,Value>
        satisfies Correspondence<Key, Value> & Iterable<Entry<Key,Value>> & 
                  Sized & Cloneable<Entries<Key,Value>> 
        given Key satisfies Equality
        given Value satisfies Equality {}

shared Entries<Natural,Element> entries<Element>(Element... sequence) 
        given Element satisfies Equality {
    
    object sequenceEntries 
            satisfies Entries<Natural,Element> {
        
        shared actual Iterator<Entry<Natural,Element>> iterator() {
            class EntryIterator(Natural from) 
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
        
        shared actual Element? value(Natural index) {
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

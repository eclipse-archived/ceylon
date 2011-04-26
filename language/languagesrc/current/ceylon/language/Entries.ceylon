shared interface Entries<in U, out V> 
        //is EnumerableEntries<U,V>
        satisfies Correspondence<U, V> & Iterable<Entry<U,V>> & 
                  Sized & Cloneable<Entries<U,V>> 
        given U satisfies Equality
        given V satisfies Equality {}

shared Entries<Natural,X> entries<X>(X... sequence) 
        given X satisfies Equality {
    
    object sequenceEntries 
            satisfies Entries<Natural,X> {
        
        shared actual Iterator<Entry<Natural,X>> iterator() {
            class EntryIterator(Natural from) 
                    satisfies Iterator<Entry<Natural,X>> {
                shared actual Entry<Natural,X>? head {
                    if (exists X x = sequence[from]) {
                        return from->x;
                    }
                    else {
                        return null;
                    }
                }
                shared actual Iterator<Entry<Natural,X>> tail {
                    return EntryIterator(from+1);
                }
            }
            return EntryIterator(0);
        }
        
        shared actual X? value(Natural index) {
            return sequence[index];
        }
        
        shared actual Natural size {
            return sequence.size;
        }
        
        shared actual Entries<Natural,X> clone {
            if (nonempty sequence) {
                return entries<X>(sequence.clone);
            }
            else {
                return entries<X>({});
            }
        }
        
    }
    
    return sequenceEntries;
    
}

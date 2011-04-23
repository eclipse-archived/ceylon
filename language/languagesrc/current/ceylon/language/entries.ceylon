shared Iterable<Entry<Natural,X>> entries<X>(X[] sequence) 
        given X satisfies Equality {
    
    object sequenceEntries satisfies Iterable<Entry<Natural,X>> {
        shared actual Iterator<Entry<Natural,X>> iterator() {
            class EntryIterator(Natural from) 
                    satisfies Iterator<Entry<Natural,X>> {
                shared actual Entry<Natural,X>? head {
                    if (exists X x = sequence[from]) {
                        return Something<Entry<Natural,X>>(from->x);
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
    }
    
    return sequenceEntries;
    
}
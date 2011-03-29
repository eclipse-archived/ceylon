shared extension class SequenceEntryIterable<X>(X[] this)
        satisfies Iterable<Entry<Natural,X>> 
        given X satisfies Equality<X> {
        
    shared actual Iterator<Entry<Natural,X>> iterator() {
        class EntryIterator(Natural from) 
                satisfies Iterator<Entry<Natural,X>> {
            shared actual Entry<Natural,X>? head {
                if (exists X x = this[from]) {
                    return from->x;
                }
                else {
                    return null;
                }
            }
            shared actual Iterable<Entry<Natural,X>> tail {
                return EntryIterator(from+1);
            }
        }
        return EntryIterator(0);
    }
    
}
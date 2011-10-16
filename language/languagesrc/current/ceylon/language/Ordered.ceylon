doc "Abstract supertype of iterable containers with a 
     well-defined iteration order."
by "Gavin"
see (Sequence, OrderedSet, OrderedMap)
shared interface Ordered<out Element>
        satisfies Iterable<Element> {
    
    doc "Determine if the Ordered container is empty, that
         is, if ordered.first is null."
    shared actual default Boolean empty {
        return !(first exists);
    }
    
    doc "The first element. This should be the same value as
         ordered.iterator.head."
    shared default Element? first {
        return iterator.head;
    }
    
}
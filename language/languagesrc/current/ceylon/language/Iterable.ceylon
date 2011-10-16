doc "Abstract supertype of containers whose elements may be 
     iterated. An iterable container need not be finite, but
     its elements must at least be countable. There may not
     be a well-defined iteration order, and so the order of
     iterated elements may not be stable."
see (Ordered, Collection)
by "Gavin"
shared interface Iterable<out Element> 
        satisfies Container {
    
    doc "An iterator for the elements belonging to the 
         container."
    shared formal Iterator<Element> iterator;
    
    doc "Determines if the iterable object is empty, that is
         to say, if iterable.iterator.head is null."
    shared actual default Boolean empty {
        return !(iterator.head exists);
    }
    
}
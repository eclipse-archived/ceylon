doc "Abstract supertype of containers whose elements may be 
     iterated. An iterable container need not be finite, but
     its elements must at least be countable, and there 
     must be a stable first element."
//todo: really?! what is the stable first element of a Set
//      or map or other unordered collection? We need to
//      factor out a separate subtype for containers with
//      a stable iteration order.
shared interface Iterable<out Element> 
        satisfies Container {
    
    doc "An iterator for the elements belonging to the 
         container."
    shared formal Iterator<Element> iterator;
    
    shared actual default Boolean empty {
        return !(first exists);
    }
    
    doc "The first element."
    shared default Element? first {
        return iterator.head;
    }

}
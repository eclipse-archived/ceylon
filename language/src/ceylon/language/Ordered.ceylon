doc "Abstract supertype of iterable containers with a 
     well-defined iteration order."
by "Gavin"
see (Sequence)
shared interface Ordered<out Element>
        satisfies Iterable<Element> {
    
    doc "The first element. This should be the same value as
         `ordered.iterator.head`."
    shared default Element? first {
        if (exists iterator) { 
            return iterator.head;
        }
        else {
            return null;
        }
    }

}

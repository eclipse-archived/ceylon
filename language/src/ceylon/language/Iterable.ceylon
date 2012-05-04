doc "Abstract supertype of containers whose elements may be 
     iterated. An iterable container need not be finite, but
     its elements must at least be countable. There may not
     be a well-defined iteration order, and so the order of
     iterated elements may not be stable."
see (Collection)
by "Gavin"
shared interface Iterable<out Element> 
        satisfies Container {
    
    doc "An iterator for the elements belonging to this 
         container."
    shared formal Iterator<Element> iterator;
    
    doc "Determines if the iterable object is empty, that is
         to say, if the iterator returns no elements."
    shared actual default Boolean empty {
        return is Finished iterator.next();
    }
    
    doc "A sequence containing the elements returned by the
         iterator."
    shared default Element[] sequence {
        value builder = SequenceBuilder<Element>();
        builder.appendAll(this...);
        return builder.sequence;
    }
    
    shared default Iterable<Result> map<Result>(Result collecting(Element elem)) {
        //TODO!!!
        throw;
    }
    
    shared default Iterable<Element> filter(Boolean selecting(Element elem)) {
        //TODO!!!
        throw;
    }
    
    shared default Result fold<Result>(Result initial,
            Result accumulating(Result partial, Element elem)) {
        //TODO!!!
        throw;
    }
    
}

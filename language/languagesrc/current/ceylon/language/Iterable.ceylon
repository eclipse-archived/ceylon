shared interface Iterable<out Element> 
        satisfies Container {
    
    doc "A sequence of objects belonging
         to the container."
    shared formal Iterator<Element> iterator();
    
    shared actual default Boolean empty {
        return !(first exists);
    }
    
    doc "The first object."
    shared default Element? first {
        return iterator().head;
    }

}
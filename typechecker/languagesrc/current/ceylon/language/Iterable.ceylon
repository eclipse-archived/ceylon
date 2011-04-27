shared interface Iterable<out X> 
        satisfies Container {
    
    doc "A sequence of objects belonging
         to the container."
    shared formal Iterator<X> iterator();
    
    shared actual default Boolean empty {
        return !(first exists);
    }
    
    doc "The first object."
    shared default X? first {
        return iterator().head;
    }

}
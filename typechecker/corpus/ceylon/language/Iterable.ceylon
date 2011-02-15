shared interface Iterable<out X> satisfies Container {
    
    doc "A sequence of objects belonging
         to the container."
    shared formal Iterator<X> iterator();
    
    shared actual default Boolean empty {
        return !(iterator().head exists);
    }

}
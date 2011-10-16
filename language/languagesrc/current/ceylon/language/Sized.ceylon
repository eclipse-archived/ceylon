doc "Abstract supertype of Containers with a finite number 
     of elements, where the number of elements can be 
     efficiently determined."
shared interface Sized 
        satisfies Container {
        
    doc "The number of elements or entries belonging to the 
         container."
    shared formal Natural size;
    
    doc "Determine if the sized container is empty, that is,
         if sized.size==0."
    shared actual default Boolean empty {
        return size==0;
    }
    
}
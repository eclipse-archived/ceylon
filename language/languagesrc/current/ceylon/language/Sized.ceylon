shared interface Sized 
        satisfies Container {
        
    doc "The number of elements or entries belonging to the 
         container."
    shared formal Natural size;
    
    shared actual default Boolean empty {
        return size==0;
    }
    
}
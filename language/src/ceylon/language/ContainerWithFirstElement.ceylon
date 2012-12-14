doc "Abstract supertype of containers which provide an 
     operation for accessing the first element, if any. A 
     container which may or may not be empty is a
     `ContainerWithFirstElement<Element,Nothing>`. A 
     container which is always empty is a 
     `ContainerWithFirstElement<Bottom,Nothing>`. A container
     which is never empty is a 
     `ContainerWithFirstElement<Element,Bottom>`."
shared interface ContainerWithFirstElement<out Element, out Null>
        satisfies Container
        given Null satisfies Nothing {
    
    doc "The first element. Should produce `null` if the 
         container is empty, that is, for any instance for
         which `empty` evaluates to `true`."
    shared formal Null|Element first;
    
    doc "The last element. Should produce `null` if the
         container is empty, that is, for any instance for
         which `empty` evaluates to `true`."
    shared formal Null|Element last;    
    
}

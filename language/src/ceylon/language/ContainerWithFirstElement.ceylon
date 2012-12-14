doc "Abstract supertype of containers which provide an 
     operation for accessing the first element, if any. A 
     container which may or may not be empty is a
     `ContainerWithFirstElement<Element,Nothing>`. A 
     container which is always empty is a 
     `ContainerWithFirstElement<Bottom,Nothing>`. A container
     which is never empty is a 
     `ContainerWithFirstElement<Element,Bottom>`."
shared interface ContainerWithFirstElement<out Element, out Null>
        given Null satisfies Nothing {
    
    doc "Determine if the container is empty, that is, if
         it has no elements."
    shared formal Boolean empty;
    
    doc "The first element. Should produce `null` if the 
         container is empty, that is, for any instance for
         which `empty` evaluates to `true`."
    shared formal Null|Element first;
    
    doc "The last element. Should produce `null` if the
         container is empty, that is, for any instance for
         which `empty` evaluates to `true`."
    shared formal Null|Element last;    
    
}

doc "Abstract supertype of objects which may or may not
     contain one of more other values, called *elements*.
     `Container` does not satisfy `Category`, because it is
     conceptually possible to have a container where the
     `contains()` operation is prohibitively inefficient.
     `Container` does not define a size, since it is possible
     to have a container of infinite or uncomputable size."
see (Sized, Category)
by "Gavin"
shared interface Container<Element> =>
        ContainerWithFirstElement<Element,Nothing>;

shared interface EmptyContainer =>
        ContainerWithFirstElement<Bottom,Nothing>;

shared interface NonemptyContainer<out Element> =>
        ContainerWithFirstElement<Element,Bottom>;

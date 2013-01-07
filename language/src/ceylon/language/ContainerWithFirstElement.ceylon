doc "Abstract supertype of containers which provide an 
     operation for accessing the first element, if any. A 
     container which may or may not be empty is a
     `ContainerWithFirstElement<Element,Null>`. A 
     container which is always empty is a 
     `ContainerWithFirstElement<Nothing,Null>`. A container
     which is never empty is a 
     `ContainerWithFirstElement<Element,Nothing>`."
shared interface ContainerWithFirstElement<out Element, out Absent>
        satisfies Category
        given Absent satisfies Null {
    
    doc "Determine if the container is empty, that is, if
         it has no elements."
    shared formal Boolean empty;
    
    doc "The first element. Should produce `null` if the 
         container is empty, that is, for any instance for
         which `empty` evaluates to `true`."
    shared formal Absent|Element first;
    
    doc "The last element. Should produce `null` if the
         container is empty, that is, for any instance for
         which `empty` evaluates to `true`."
    shared formal Absent|Element last;    
    
}

doc "Abstract supertype of objects which may or may not
     contain one of more other values, called *elements*."
see (Category)
by "Gavin"
shared interface Container<out Element> =>
        ContainerWithFirstElement<Element,Null>;

shared interface EmptyContainer =>
        ContainerWithFirstElement<Nothing,Null>;

shared interface NonemptyContainer<out Element> =>
        ContainerWithFirstElement<Element,Nothing>;

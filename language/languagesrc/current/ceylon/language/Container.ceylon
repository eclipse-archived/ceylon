doc "Abstract supertype of objects which may or may not
     contain one of more other values, called elements.
     Container does not satisfy Category, because it is
     conceptually possible to have a container where the
     contains() operation is prohibitively inefficient.
     Container does not define a size, since it is possible
     to have a container of infinite or uncomputable size."
see (Sized, Category)
by "Gavin"
shared interface Container {
        
    doc "Determine if the container is empty, that is, if
         it has no elements."
    shared formal Boolean empty;
    
}
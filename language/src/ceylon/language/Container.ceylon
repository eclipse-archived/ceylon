"Abstract supertype of objects which may or may not
 contain one of more other values, called *elements*,
 and provide an operation for accessing the first 
 element, if any. A container which may or may not be 
 empty is a `Container<Element,Null>`. A container which 
 is always empty is a `Container<Nothing,Null>`. A 
 container which is never empty is a 
 `Container<Element,Nothing>`."
see (`interface Category`)
by ("Gavin")
deprecated ("Will be removed in Ceylon 1.0.")
shared interface Container<out Element, out Absent=Null>
        satisfies Category
        given Absent satisfies Null {
    
    "Determine if the container is empty, that is, if
     it has no elements."
    shared formal Boolean empty;
    
    "The first element. Should produce `null` if the 
     container is empty, that is, for any instance for
         which `empty` evaluates to `true`."
    shared formal Absent|Element first;
    
    "The last element. Should produce `null` if the
     container is empty, that is, for any instance for
     which `empty` evaluates to `true`."
    shared formal Absent|Element last;    
    
}

"An empty container."
deprecated ("Will be removed in Ceylon 1.0.")
shared interface EmptyContainer => Container<Nothing,Null>;

"A nonempty container."
deprecated ("Will be removed in Ceylon 1.0.")
shared interface NonemptyContainer<out Element> =>
        Container<Element,Nothing>;

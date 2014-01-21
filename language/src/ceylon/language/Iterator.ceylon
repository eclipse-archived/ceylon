"Produces elements of an [[Iterable]] object."
see (`interface Iterable`)
by ("Gavin")
shared interface Iterator<out Element> {
    "The next element, or [[finished]] if there are no more 
     elements to be iterated."
    shared formal Element|Finished next();
}

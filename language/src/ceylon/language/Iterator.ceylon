doc "Produces elements if an `Iterable` object. Classes that 
     implement this interface should be immutable."
see (Iterable)
by "Gavin"
shared interface Iterator<out Element> {
    doc "The next element, or `finished` if
         there are no more elements to be iterated."
    shared formal Element|Finished next();
}

shared object finished extends Finished() {}

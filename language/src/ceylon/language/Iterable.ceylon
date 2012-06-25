doc "Abstract supertype of containers whose elements may be 
     iterated. An iterable container need not be finite, but
     its elements must at least be countable. There may not
     be a well-defined iteration order, and so the order of
     iterated elements may not be stable."
see (Collection)
by "Gavin"
shared interface Iterable<out Element> 
        satisfies Container {

    doc "An iterator for the elements belonging to this 
         container."
    shared formal Iterator<Element> iterator;

    doc "Determines if the iterable object is empty, that is
         to say, if the iterator returns no elements."
    shared actual default Boolean empty {
        return is Finished iterator.next();
    }

    doc "A sequence containing the elements returned by the
         iterator."
    shared default Element[] sequence {
        return { this... };
    }

    doc "An `Iterable` containing the results of applying
         the given mapping to the elements of to this 
         container."
    shared default Iterable<Result> map<Result>(
            doc "The mapping to apply to the elements."
            Result collecting(Element elem)) {
        return elements(for (elem in this) collecting(elem));
    }

    doc "An `Iterable` containing the elements of this 
         container that satisfy the given predicate."
    shared default Iterable<Element> filter(
            doc "The predicate the elements must satisfy."
            Boolean selecting(Element elem)) {
        return elements(for (elem in this) if (selecting(elem)) elem);
    }

    doc "The result of applying the accumulating function to 
         each element of this container in turn." 
    shared default Result fold<Result>(Result initial,
            doc "The accumulating function that accepts an
                 intermediate result, and the next element."
            Result accumulating(Result partial, Element elem)) {
        variable value r := initial;
        for (e in this) {
            r := accumulating(r, e);
        }
        return r;
    }

    doc "The first element which satisfies the given 
         predicate, if any."
    shared default Element? find(
            doc "The predicate the element must satisfy."
            Boolean selecting(Element elem)) {
        for (e in this) {
            if (selecting(e)) {
                return e;
            }
        }
        return null;
    }
 
    doc "A sequence containing the elements of this
         container, sorted according to a function 
         imposing a partial order upon the elements."
    shared default Element[] sorted(
            doc "The function comparing pairs of elements."
            Comparison? comparing(Element x, Element y)) { throw; }
 
}

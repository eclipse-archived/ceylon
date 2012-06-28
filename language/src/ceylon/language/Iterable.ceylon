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

    doc "Returns true if at least one element satisfies the
         predicate function."
    shared default Boolean any(
            doc "The function that evaluates an Element of
                 the container."
            Boolean selecting(Element e)) {
        for (e in this) {
            if (selecting(e)) {
                return true;
            }
        }
        return false;
    }

    doc "Returns true if all elements satisfy the predicate
         function."
    shared default Boolean every(
            doc "The function that evaluates an Element of
                 the container."
            Boolean selecting(Element e)) {
        for (e in this) {
            if (!selecting(e)) {
                return false;
            }
        }
        return true;
    }

    doc "Returns an `Iterable` that will skip the specified
         number of elements from the beginning of this 
         `Iterable`."
    shared default Iterable<Element> skipping(Integer skip) {
        if (skip <= 0) { return this; }
        variable value i:=0;
        return elements { for (e in this) if (i++>skip) e };
    }

    doc "Returns an `Iterable` that will only return the
         specified number of elements from the beginning
         of this `Iterable`."
    shared default Iterable<Element> taking(Integer take) {
        if (take <= 0) { return {}; }
        variable value i:=0;
        return elements { for (e in this) if (i++<take) e };
    }
    
    doc "Returns an `Iterable` that returns elements from the
         recipient moving forward by the specified step size.
         A step size of 1 results in an Iterable identical to the
         original; a step size of 0 results in an Iterable that
         will always return the recipient's first element.
         The step size must be greater than or equal to zero."
    shared default Iterable<Element> by(Integer step) {
        if (step <= 0) {
            //TODO: implement specified behavior for step==0
            throw Exception("step size less than 1");
        } 
        else if (step == 1) {
            return this;
        } 
        else {
           variable value i:=0;
           return elements { for (e in this) if (i%step==0) e };
        }
    }

}

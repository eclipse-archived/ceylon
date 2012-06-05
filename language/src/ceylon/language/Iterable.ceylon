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
    shared default Boolean empty {
        return is Finished iterator.next();
    }

    doc "A sequence containing the elements returned by the
         iterator."
    shared default Element[] sequence {
        value builder = SequenceBuilder<Element>();
        builder.appendAll(this...);
        return builder.sequence;
    }

    doc "Returns an Iterable that will return the transformation of the original elements."
    shared default Iterable<Result> map<Result>(Result collecting(Element elem)) {
        object mapped satisfies Iterable<Result> {
            shared actual Iterator<Result> iterator {
                object mappedIterator satisfies Iterator<Result> {
                    Iterator<Element> iter = outer.iterator;
                    shared actual Result|Finished next() {
                        value e = iter.next();
                        if (is Element x=e) {
                            return collecting(x);
                        } else {
                            return exhausted;
                        }
                    }
                }
                return mappedIterator;
            }
        }
        return mapped;
    }

    doc "Returns an Iterable that returns only the elements which satisfy the collecting condition."
    shared default Iterable<Element> filter(Boolean selecting(Element elem)) {
        object filtered satisfies Iterable<Element> {
            shared actual Iterator<Element> iterator {
                object filteredIterator satisfies Iterator<Element> {
                    Iterator<Element> iter = outer.iterator;
                    shared actual Element|Finished next() {
                        variable Element|Finished e := iter.next();
                        variable Boolean flag;
                        if (is Element x=e) {
                            flag := selecting(x);
                        } else {
                            flag := true;
                        }
                        while (!flag) {
                            e := iter.next();
                            if (is Element x=e) {
                                flag := selecting(x);
                            } else {
                                flag := true;
                            }
                        }
                        return e;
                    }
                }
                return filteredIterator;
            }
        }
        return filtered;
    }

    doc "Returns the result of applying the accumulating function to each element." 
    shared default Result fold<Result>(Result initial,
            Result accumulating(Result partial, Element elem)) {
        variable value r := initial;
        for (e in this) {
            r := accumulating(r, e);
        }
        return r;
    }

    doc "Return the first element which satisfies the selecting function, if any."
    shared default Element? find(Boolean selecting(Element elem)) {
        for (e in this) {
            if (selecting(e)) {
                return e;
            }
        }
        return null;
    }
 
}

doc "An iterator which always returns `exhausted`."
object emptyIterator satisfies Iterator<Bottom> {
    shared actual Finished next() {
        return exhausted;
    }
}

doc "A fixed-size collection with no elements."
shared interface None<out Element>
        satisfies FixedSized<Element> {

    doc "Returns `null`."
    shared actual Nothing first {
        return null;
    }

    doc "Returns an emptyIterator."
    shared actual default Iterator<Element> iterator {
        return emptyIterator;
    }

    doc "Returns 0."
    shared actual default Integer size {
        return 0;
    }

    doc "Returns `true`."
    shared actual Boolean empty {
        return true;
    }

}

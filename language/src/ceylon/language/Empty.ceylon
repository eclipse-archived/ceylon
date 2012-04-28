doc "A sequence with no elements. The type of the expression
     `{}`."
see (Sequence)
shared interface Empty
           satisfies Ranged<Integer,Empty> &
                     List<Bottom> & None<Bottom> &
                     Cloneable<Empty> {

    doc "Returns 0."
    shared actual Integer size { return 0; }

    doc "Returns an iterator that is already exhausted."
    shared actual Iterator<Bottom> iterator {
        return emptyIterator;
    }

    doc "Returns null for any given key."
    shared actual Nothing item(Integer key) {
        return null;
    }

    doc "Returns an Empty for any given segment."
    shared actual Empty segment(Integer from, Integer length) {
        return this;
    }

    doc "Returns an Empty for any given span."
    shared actual Empty span(Integer from, Integer? to) {
        return this;
    }

    doc "Returns a string description of the empty List: `{}`"
    shared actual String string {
        return "{}";
    }
    doc "Returns null."
    shared actual Nothing lastIndex { return null; }

    //shared actual Empty rest { return this; }

    doc "Returns an Empty."
    shared actual Empty clone {
        return this;
    }

    doc "Returns false for any given element."
    shared actual Boolean contains(Object element) {
        return false;
    }

    doc "Returns 0 for any given element."
    shared actual Integer count(Object element) {
        return 0;
    }

    shared actual Boolean defines(Integer index) {
        return false;
    }

}
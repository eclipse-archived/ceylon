doc "A sequence with no elements. The type of the expression
     `{}`."
see (Sequence)
shared interface Empty
           satisfies List<Bottom> & None<Bottom> &
                     Ranged<Integer,Empty> &
                     Cloneable<Empty> {

    doc "Returns 0."
    shared actual Integer size { return 0; }

    doc "Returns an iterator that is already exhausted."
    shared actual Iterator<Bottom> iterator {
        return emptyIterator;
    }

    doc "Returns `null` for any given index."
    shared actual Nothing item(Integer index) {
        return null;
    }

    doc "Returns an `Empty` for any given segment."
    shared actual Empty segment(Integer from, Integer length) {
        return this;
    }

    doc "Returns an `Empty` for any given span."
    shared actual Empty span(Integer from, Integer? to) {
        return this;
    }

    doc "Returns an `Empty`."
    shared actual Empty reversed {
	    return this;
    }

    doc "Returns an `Empty`."
    shared actual Empty sequence {
        return this;
    }

    doc "Returns a string description of the empty List: `{}`."
    shared actual String string {
        return "{}";
    }
    doc "Returns `null`."
    shared actual Nothing lastIndex { return null; }

    shared actual Nothing first { return null; }

    //shared actual Empty rest { return this; }

    doc "Returns an `Empty`."
    shared actual Empty clone {
        return this;
    }

    doc "Returns `false` for any given element."
    shared actual Boolean contains(Object element) {
        return false;
    }

    doc "Returns 0 for any given predicate."
    shared actual Integer count(Boolean selecting(Bottom element)) {
        return 0;
    }

    shared actual Boolean defines(Integer index) {
        return false;
    }

    shared actual Empty map<Result>(Result selecting(Bottom element)) {
        return this;
    }
    shared actual Empty filter(Boolean selecting(Bottom element)) {
        return this;
    }
    shared actual Result fold<Result>(Result initial,
            Result accumulating(Result partial, Bottom element)) {
        return initial;
    }
    shared actual Nothing find(Boolean selecting(Bottom element)) {
        return null;
    }
    shared actual Empty sorted(Comparison? comparing(Bottom a, Bottom b)) {
        return this;
    }
    shared actual Boolean any(Boolean selecting(Bottom element)) {
        return false;
    }
    shared actual Boolean every(Boolean selecting(Bottom element)) {
        return false;
    }
    shared actual Empty skipping(Integer skip) {
        return this;
    }
    shared actual Empty taking(Integer take) {
        return this;
    }
    shared actual Empty by(Integer step) {
        return this;
    }
    shared actual Empty coalesced {
        return this;
    }
    shared actual List<Other> withLeading<Other>(Other... others) {
        return others.sequence;
    }
    shared actual List<Other> withTrailing<Other>(Other... others) {
        return others.sequence;
    }
}
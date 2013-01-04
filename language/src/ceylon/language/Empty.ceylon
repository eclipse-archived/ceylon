doc "A sequence with no elements. The type of the expression
     `{}`."
see (Sequence)
shared interface Empty
           satisfies Bottom[] & 
                     EmptyContainer &
                     Ranged<Integer,[]> &
                     Cloneable<Empty> {
    
    doc "Returns an iterator that is already exhausted."
    shared actual Iterator<Bottom> iterator => emptyIterator;
    
    doc "Returns `null` for any given index."
    shared actual Nothing item(Integer index) => null;
    
    doc "Returns an `Empty` for any given segment."
    shared actual Empty segment(Integer from, Integer length) => this;
    
    doc "Returns an `Empty` for any given span."
    shared actual Empty span(Integer from, Integer to) => this;
    doc "Returns an `Empty` for any given span."
    shared actual Empty spanTo(Integer to) => this;
    doc "Returns an `Empty` for any given span."
    shared actual Empty spanFrom(Integer from) => this;
    
    doc "Returns `true`."
    shared actual Boolean empty => true;
    
    doc "Returns 0."
    shared actual Integer size => 0; 
    
    doc "Returns an `Empty`."
    shared actual Empty reversed => this;
    
    doc "Returns an `Empty`."
    shared actual Empty sequence => this;
    
    doc "Returns a string description of the empty sequence: 
         `{}`."
    shared actual String string => "{}";
    
    doc "Returns `null`."
    shared actual Nothing lastIndex => null; 
    
    doc "Returns `null`."
    shared actual Nothing first => null;
    
    doc "Returns `null`."
    shared actual Nothing last => null; 
    
    //shared actual Empty rest { return this; }
    
    doc "Returns an `Empty`."
    shared actual Empty clone => this;
    
    doc "Returns an `Empty`."
    shared actual Empty coalesced => this; 
    
    doc "Returns an `Empty`."
    shared actual Empty indexed => this;
    
    doc "Returns `other`."
    shared actual Iterable<Other> chain<Other>(Iterable<Other> other) => other;
    
    doc "Returns `false` for any given element."
    shared actual Boolean contains(Object element) => false;
    
    doc "Returns 0 for any given predicate."
    shared actual Integer count(
            Boolean selecting(Bottom element)) => 0;
    
    shared actual Boolean defines(Integer index) => false;
    
    shared actual Empty map<Result>(
            Result collecting(Bottom element)) => this;
    
    shared actual Empty filter
            (Boolean selecting(Bottom element)) => this;
    
    shared actual Result fold<Result>(Result initial,
            Result accumulating(Result partial, Bottom element)) => 
                    initial;
    
    shared actual Nothing find
            (Boolean selecting(Bottom element)) => null;
    
    shared actual Empty sort
            (Comparison? comparing(Bottom a, Bottom b)) => this;
    
    shared actual Empty collect<Result>
            (Result collecting(Bottom element)) => this;
    
    shared actual Empty select
            (Boolean selecting(Bottom element)) => this;
    
    shared actual Boolean any
            (Boolean selecting(Bottom element)) => false;
    
    shared actual Boolean every
            (Boolean selecting(Bottom element)) => false;
    
    shared actual Empty skipping(Integer skip) => this;
    
    shared actual Empty taking(Integer take) => this;
    
    shared actual Empty by(Integer step) => this;
    
    shared actual Sequence<Element> withLeading<Element>
            (Element element) => [ element ];
    
    shared actual Sequence<Element> withTrailing<Element>
            (Element element) => [ element ];
}

doc "The value representing a sequence with no elements. The 
     instance of `{}`"
//by "Tako Schotanus"
shared object empty extends Object() satisfies Empty {}

doc "An iterator that returns no elements."
shared object emptyIterator satisfies Iterator<Bottom> {
    next() => exhausted;
}

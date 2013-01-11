doc "A sequence with no elements. The type of the expression
     `{}`."
see (Sequence)
shared interface Empty
           satisfies Nothing[] &
                     Ranged<Integer,[]> &
                     Cloneable<Empty> {
    
    doc "Returns an iterator that is already exhausted."
    shared actual Iterator<Nothing> iterator => emptyIterator;
    
    doc "Returns `null` for any given index."
    shared actual Null item(Integer index) => null;
    
    doc "Returns an `Empty` for any given segment."
    shared actual [] segment(Integer from, Integer length) => this;
    
    doc "Returns an `Empty` for any given span."
    shared actual [] span(Integer from, Integer to) => this;
    doc "Returns an `Empty` for any given span."
    shared actual [] spanTo(Integer to) => this;
    doc "Returns an `Empty` for any given span."
    shared actual [] spanFrom(Integer from) => this;
    
    doc "Returns `true`."
    shared actual Boolean empty => true;
    
    doc "Returns 0."
    shared actual Integer size => 0; 
    
    doc "Returns an `Empty`."
    shared actual [] reversed => this;
    
    doc "Returns an `Empty`."
    shared actual [] sequence => this;
    
    doc "Returns a string description of the empty sequence: 
         `{}`."
    shared actual String string => "{}";
    
    doc "Returns `null`."
    shared actual Null lastIndex => null; 
    
    doc "Returns `null`."
    shared actual Null first => null;
    
    doc "Returns `null`."
    shared actual Null last => null; 
    
    //shared actual [] rest { return this; }
    
    doc "Returns an `Empty`."
    shared actual [] clone => this;
    
    doc "Returns an `Empty`."
    shared actual [] coalesced => this; 
    
    doc "Returns an `Empty`."
    shared actual [] indexed => nothing; //this;
    
    doc "Returns `other`."
    shared actual {Other*} chain<Other>({Other*} other) => other;
    
    doc "Returns `false` for any given element."
    shared actual Boolean contains(Object element) => false;
    
    doc "Returns 0 for any given predicate."
    shared actual Integer count(
            Boolean selecting(Nothing element)) => 0;
    
    shared actual Boolean defines(Integer index) => false;
    
    shared actual [] map<Result>(
            Result collecting(Nothing element)) => nothing; //this;
    
    shared actual [] filter
            (Boolean selecting(Nothing element)) => this;
    
    shared actual Result fold<Result>(Result initial,
            Result accumulating(Result partial, Nothing element)) => 
                    initial;
    
    shared actual Null find
            (Boolean selecting(Nothing element)) => null;
    
    shared actual [] sort
            (Comparison? comparing(Nothing a, Nothing b)) => this;
    
    shared actual [] collect<Result>
            (Result collecting(Nothing element)) => this;
    
    shared actual [] select
            (Boolean selecting(Nothing element)) => this;
    
    shared actual Boolean any
            (Boolean selecting(Nothing element)) => false;
    
    shared actual Boolean every
            (Boolean selecting(Nothing element)) => false;
    
    shared actual [] skipping(Integer skip) => this;
    
    shared actual [] taking(Integer take) => this;
    
    shared actual [] by(Integer step) => this;
    
    shared actual Sequence<Element> withLeading<Element>
            (Element element) => [ element ];
    
    shared actual Sequence<Element> withTrailing<Element>
            (Element element) => [ element ];
}

doc "The value representing a sequence with no elements. The 
     instance of `{}`"
//by "Tako Schotanus"
shared object empty extends Object() satisfies [] {}

doc "An iterator that returns no elements."
shared object emptyIterator satisfies Iterator<Nothing> {
    next() => finished;
}

import ceylon.language { e=empty }

"A sequence with no elements. The type `Empty` may be
 abbreviated `[]`, and an instance is produced by the 
 expression `[]`. That is, in the following expression,
 `none` has type `[]` and refers to the value `[]`:
 
     [] none = [];
 
 (Whether the syntax `[]` refers to the type or the value 
 depends upon how it occurs grammatically.)"
see (`interface Sequence`)
shared interface Empty of e
           satisfies Nothing[] &
                     Ranged<Integer,Nothing,[]> {
    
    "Returns an iterator that is already exhausted."
    shared actual Iterator<Nothing> iterator() 
            => emptyIterator;
    
    "Returns `null` for any given index."
    shared actual Null get(Integer index) => null;
    
    "Returns `null` for any given index."
    shared actual Null getFromLast(Integer index) => null;
    
    "Returns `null` for any given index."
    shared actual Null getFromFirst(Integer index) => null;
    
    "Returns an `Empty`."
    shared actual [] keys => this;
    
    "Returns an `Empty` for any given segment."
    shared actual [] segment(Integer from, Integer length) 
            => this;
    
    "Returns an `Empty` for any given span."
    shared actual [] span(Integer from, Integer to) => this;
    
    "Returns an `Empty` for any given span."
    shared actual [] spanTo(Integer to) => this;
    
    "Returns an `Empty` for any given span."
    shared actual [] spanFrom(Integer from) => this;
    
    "Returns `true`."
    shared actual Boolean empty => true;
    
    "Returns 0."
    shared actual Integer size => 0; 
    
    "Returns an `Empty`."
    shared actual [] reversed => this;
    
    "Returns an `Empty`."
    shared actual [] sequence() => this;
    
    "Returns a string description of the empty sequence: 
     `[]`."
    shared actual String string => "[]";
    
    "Returns `null`."
    shared actual Null lastIndex => null; 
    
    "Returns `null`."
    shared actual Null first => null;
    
    "Returns `null`."
    shared actual Null last => null; 
    
    "Returns an `Empty`."
    shared actual [] rest => this;
    
    "Returns an `Empty`."
    shared actual [] clone() => this;
    
    "Returns an `Empty`."
    shared actual [] coalesced => this; 
    
    "Returns an `Empty`."
    shared actual [] indexed => this;
    
    "Returns the given [[other]] iterable object."
    shared actual Iterable<Other,OtherAbsent> 
    chain<Other,OtherAbsent>(Iterable<Other,OtherAbsent> other) 
            given OtherAbsent satisfies Null 
            => other;
    
    shared actual [] defaultNullElements<Default>
            (Default defaultValue) 
            => this;
    
    shared actual [] cycle(Integer times) => this;
    
    shared actual [] repeat(Integer times) => this;
    
    shared actual [] cycled => this;
    
    "Returns `false` for any given element."
    shared actual Boolean contains(Object element) => false;
    
    "Returns 0 for any given predicate."
    shared actual Integer count(
            Boolean selecting(Nothing element)) => 0;
    
    shared actual Boolean defines(Integer index) => false;
    
    shared actual [] map<Result>(
            Result collecting(Nothing element)) => this;
    
    shared actual Callable<[],Args> spread<Result,Args>(
        Callable<Result,Args> method(Nothing element))
            given Args satisfies Anything[] 
            => flatten((Args args) => this);
    
    shared actual [] filter(Boolean selecting(Nothing element)) 
            => this;
    
    shared actual Result fold<Result>(Result initial,
            Result accumulating(Result partial, Nothing element)) 
            => initial;
    
    shared actual Null reduce<Result>(
            Result accumulating(Result partial, Nothing element)) 
            => null;
    
    shared actual Null find
            (Boolean selecting(Nothing element)) => null;
    
    shared actual [] sort
            (Comparison comparing(Nothing a, Nothing b)) 
            => this;
    
    shared actual [] collect<Result>
            (Result collecting(Nothing element)) => this;
    
    shared actual [] select
            (Boolean selecting(Nothing element)) => this;
    
    shared actual Boolean any
            (Boolean selecting(Nothing element)) => false;
    
    shared actual Boolean every
            (Boolean selecting(Nothing element)) => true;
    
    shared actual [] skip(Integer skipping) => this;
    
    shared actual [] take(Integer taking) => this;
    
    shared actual [] skipWhile(Boolean skipping(Nothing elem)) 
            => this;
    
    shared actual [] takeWhile(Boolean taking(Nothing elem)) 
            => this;
    
    shared actual [] by(Integer step) => this;
    
    shared actual [Other] withLeading<Other>(Other element) 
            => [element];
    
    shared actual [Other] withTrailing<Other>(Other element) 
            => [element];
    
    shared actual [Other*] append<Other>({Other*} elements) 
            => [*elements];
    
    shared actual {Other+} follow<Other>(Other head) 
            => { head };
    
    shared actual List<Other> extend<Other>(List<Other> list)
            => list;
    
    shared actual [] sublist(Integer from, Integer to) => this;
    
    shared actual [] sublistFrom(Integer from) => this;
    
    shared actual [] sublistTo(Integer to) => this;
    
    shared actual [] initial(Integer length) => this;
    
    shared actual [] terminal(Integer length) => this;
    
    shared actual [] indexesWhere(
        Boolean selecting(Nothing element)) 
            => this;
    
    shared actual Null firstIndexWhere(
        Boolean selecting(Nothing element)) 
            => null;
    
    shared actual Null lastIndexWhere(
        Boolean selecting(Nothing element)) 
            => null;
    
    shared actual Boolean includes(List<Anything> sublist) 
            => sublist.empty;
    
    shared actual [] trim(Boolean trimming(Nothing elem)) 
            => this;
    
    shared actual [] trimLeading(Boolean trimming(Nothing elem)) 
            => this;
    
    shared actual [] trimTrailing(Boolean trimming(Nothing elem)) 
            => this;
    
}

"A sequence with no elements, abbreviated `[]`. The unique 
 instance of the type `[]`."
by ("Tako Schotanus")
shared object empty 
		extends Object() 
		satisfies [] {}

"An iterator that returns no elements."
shared object emptyIterator 
		satisfies Iterator<Nothing> {
    next() => finished;
}

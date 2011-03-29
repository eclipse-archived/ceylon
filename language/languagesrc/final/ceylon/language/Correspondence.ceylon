shared interface Correspondence<in U, out V>
        is EnumerableCorrespondence<U,V>
        given U satisfies Equality<U> 
        given V satisfies Equality<V> {
    
    doc "Binary lookup operator x[key]. Returns the value defined
         for the given key, or |null| if there is no value defined
         for the given key."
    shared formal Gettable<V?> value(U key);
    
    doc "Binary sequenced lookup operator |x[keys]|. Return a list 
         of values defined for the given keys, in order."
    throws (UndefinedKeyException
            -> "if no value is defined for one of the given 
                keys")
    shared default V[] values(U... keys) {
        return from (U key in keys) select (this[key]);
    }
    
    doc "Binary iterated lookup operator |x[keys]|. Return an iterator
         of the values defined for the given keys."
    throws (UndefinedKeyException
            -> "if no value is defined for one of the given 
                keys")
    shared default Iterable<V> values(Iterable<U> keys) {
        return from (U key in keys) select (this[key]);
    }
    
    doc "Determine if there are values defined for the given keys.
         Return |true| iff there are values defined for all the
         given keys."
    shared default Boolean defines(U... keys) {
        return forAll (U key in keys) every (this[key] exists);
    }
    
}
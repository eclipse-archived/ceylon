shared interface Correspondence<in U, out V>
        //is EnumerableCorrespondence<U,V>
        given U satisfies Equality {
    
    doc "Binary lookup operator x[key]. Returns the value defined
         for the given key, or |null| if there is no value defined
         for the given key."
    shared formal V? value(U key);
        
}
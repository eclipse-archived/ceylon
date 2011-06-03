shared interface Correspondence<in Key, out Value>
        given Key satisfies Equality {
    
    doc "Binary lookup operator x[key]. Returns the value defined
         for the given key, or |null| if there is no value defined
         for the given key."
    shared formal Value? value(Key key);
        
}
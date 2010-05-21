public mutable interface OpenCorrespondence<in U, V> 
        satisfies Correspondence<U, V> {
        
     doc "Element assignment operator x[key] = value. Assign a value 
          to the given key. Return the previous value for the key,
          or null if there was no value defined."
     public optional V define(U key, V value);

}
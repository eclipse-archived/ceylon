public extension class OpenCorrespondences<in U, V>(OpenCorrespondence<U, V> correspondence) {

     doc "Add the given entries, overriding any definitions that 
          already exist."
     throws (UndefinedKeyException
             -> "if a value can not be defined for one of the given 
                 keys")
     public void define(Entry<U, V>... definitions) {
         for (U key->V value) {
             correspondence.define(key, value);
         }
     }
    
}
public extension class Correspondences<in U, out V>(Correspondence<U, V> correspondence) {

    doc "Binary list lookup operator x[keys]. Returns a list of 
         values defined for the given keys, in order."
    throws #UndefinedKeyException
           "if no value is defined for one of the given keys"
    public List<V> values(List<U> keys) {
        return from (U key in keys) select correspondence.lookup(key)
    }
    
    doc "Binary set lookup operator x[keys]. Returns a set of 
         values defined for the given set of keys."
    throws #UndefinedKeyException
           "if no value is defined for one of the given keys"
    public Set<V> values(Set<U> keys) {
        return ( from (U key in keys) select correspondence.lookup(key) ).elements
    }
    
}
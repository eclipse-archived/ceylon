public interface Correspondence<in U, out V> {
    
    doc "Binary lookup operator x[key]. Returns the value defined
         for the given key."
    throws #UndefinedKeyException
           "if no value is defined for the given key"
    public V value(U key);
    
    doc "Determine if there are values defined for the given keys.
         Return |true| iff there are values defined for all the
         given keys."
    public Boolean defines(U... keys);
    
}
public interface Map<U, out V>
        satisfies Collection<Entry<U,V>>, Correspondence<U, V> {
    
    doc "The keys of the map, as a |Set|."
    public Set<U> keys;
    
    doc "The values of the map, as a |Bag|."
    public Bag<V> values;
    
    doc "A |Map| of each value belonging to the map, to the 
         |Set| of all keys at which that value occurs."
    public Map<V, Set<U>> inverse;
    
    doc "Return the value defined for the given key, or null
         if no value is defined for that key."
    public V? valueOrNull(U key);
    
    doc "Produce a new map by applying an operation to every 
         element of the map."
    public Map<U, W> transform<W>(W? select(U key -> V value));
    
    doc "The entries of the map for which the given condition 
         evaluates to |true|, as a |Map|."
    public Map<U, V> entries(Boolean having(U key -> V value));
    
    public override Map<U, T> with<T>(Entry<U, T>... entries) given T abstracts V;
    public override OpenMap<U,T> copy<T>() given T abstracts V;
    
}
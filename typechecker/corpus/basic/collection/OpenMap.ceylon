public mutable interface OpenMap<U,V> 
        satisfies Map<U,V>, OpenCollection<Entry<U,V>>, OpenCorrespondence<U, V> {
    
    doc "Remove the entry for the given key, returning the 
         value of the removed entry."
    throws (UndefinedKeyException
            -> "if no value is defined for the given key")    
    public V remove(U key);
    
    doc "Remove all entries from the map which have keys for 
         which the given condition evaluates to |true|. Return 
         entries which were removed."
    public Map<U,V> remove(Boolean having(U key));
    
    override public OpenSet<U> keys;
    override public OpenBag<V> values;
    override public OpenMap<V, Set<U>> inverse;
    
}
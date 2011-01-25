shared class Entry<out U, out V>(U key, V value) 
        extends Object() 
        satisfies Equality<Entry<U,V>>
        given U satisfies Equality<U> 
        given V satisfies Equality<V> {
    
    doc "The key used to access the entry."
    shared U key = key;
    
    doc "The value associated with the key."
    shared V value = value;
    
    shared actual Boolean equals(Entry<U,V> that) {
        return this.key==that.key && this.value==that.value
    }
    
    shared actual Integer hash = 0;

}
public class Entry<out U, out V>(U key, V value) {
    
    doc "The key used to access the entry."
    public U key = key;
    
    doc "The value associated with the key."
    public V value = value;
    
    override public Boolean equals(Object that) {
        return equals(that, #key, #value)
    }
    
    override public Integer hash {
        return hash(#key, #value)
    }

}
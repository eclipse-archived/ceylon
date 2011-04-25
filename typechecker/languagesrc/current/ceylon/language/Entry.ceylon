shared abstract class Entry<U, out V>(U key, V value)
        extends Object()
        satisfies Equality
        given U satisfies Equality
        given V satisfies Equality {
    
    doc "The key used to access the entry."
    shared U key = key;
    
    doc "The value associated with the key."
    shared V value = value;
    
    shared actual Integer hash = key.hash/2.integer + value.hash/2.integer; //TODO: really should be xor 
    
    shared actual Boolean equals(Object that) {
        if (is Entry<Equality,Equality> that) {
            return this.key==that.key && this.value==that.value;
        }
        else {
            return false;
        }
    }
    
}
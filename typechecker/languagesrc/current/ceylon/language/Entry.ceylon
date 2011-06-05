shared abstract class Entry<out Key, out Value>(Key key, Value value)
        extends Object()
        satisfies Equality
        given Key satisfies Equality
        given Value satisfies Equality {
    
    doc "The key used to access the entry."
    shared Key key = key;
    
    doc "The value associated with the key."
    shared Value value = value;
    
    shared actual Integer hash = key.hash/2 + value.hash/2; //TODO: really should be xor
    
    shared actual Boolean equals(Equality that) {
        if (is Entry<Equality,Equality> that) {
            return this.key==that.key && this.value==that.value;
        }
        else {
            return false;
        }
    }
    
}
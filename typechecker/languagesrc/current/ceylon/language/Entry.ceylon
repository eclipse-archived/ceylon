shared abstract class Entry<out Key, out Item>(Key key, Item item)
        extends Object()
        satisfies Equality
        given Key satisfies Equality
        given Item satisfies Equality {
    
    doc "The key used to access the entry."
    shared Key key = key;
    
    doc "The value associated with the key."
    shared Item item = item;
    
    shared actual Integer hash = key.hash/2 + item.hash/2; //TODO: really should be xor
    
    shared actual Boolean equals(Equality that) {
        if (is Entry<Equality,Equality> that) {
            return this.key==that.key && this.item==that.item;
        }
        else {
            return false;
        }
    }
    
}
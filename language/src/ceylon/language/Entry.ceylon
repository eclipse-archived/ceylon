doc "A pair containing a key and an associated value
     called the item. Used primarily to represent the
     elements of a `Map`."
by "Gavin"
shared class Entry<out Key, out Item>(key, item)
        extends Object()
        given Key satisfies Object
        given Item satisfies Object {
    
    doc "The key used to access the entry."
    shared Key key;
    
    doc "The value associated with the key."
    shared Item item;
    
    doc "Determines if this entry is equal to the given
         entry. Two entries are equal if they have the same
         key and the same value."
    shared actual Boolean equals(Object that) {
        if (is Entry<Object,Object> that) {
            return this.key==that.key &&
                    this.item==that.item;
        }
        else {
            return false;
        }
    }
    
    shared actual Integer hash => 
            (31 + key.hash) * 31 + item.hash;
    
    doc "Returns a description of the entry in the form 
         `key->item`."
    shared actual String string =>
            key.string + "->" + item.string;
    
}

"A `Map` implementation that wraps an `Iterable` of 
 entries. All operations, such as lookups, size, etc. 
 are performed on the `Iterable`."
by ("Enrique Zamudio")
shared class LazyMap<out Key,out Item>(entries)
    satisfies Map<Key,Item>
        given Key satisfies Object
        given Item satisfies Object {
    
    "The entries in the map, which must have distinct keys."
    {<Key->Item>*} entries;
    
    first => entries.first;
    last => entries.last;
    
    clone => this;
    
    shared actual default Integer size => entries.size;
    
    shared actual default Item? get(Object key) {
        for (k->i in entries) {
            if (key==k) {
                return i;
            }
        }
        return null;
    }
    
    shared actual default Boolean defines(Object key) {
        for (k->i in entries) {
            if (key==k) {
                return true;
            }
        }
        return false;
    }
    
    iterator() => entries.iterator();
    
    equals(Object that) => (super of Map<Key,Item>).equals(that);
    hash => (super of Map<Key,Item>).hash;
    
}

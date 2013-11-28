"A `Map` implementation that wraps an `Iterable` of 
 entries. All operations, such as lookups, size, etc. 
 are performed on the `Iterable`."
by ("Enrique Zamudio")
shared class LazyMap<out Key,out Item>({<Key->Item>*} entries)
    satisfies Map<Key,Item>
        given Key satisfies Object
        given Item satisfies Object {
    
    first => entries.first;
    last => entries.last;
    
    clone => this;
    
    size => entries.size;
    
    get(Object key) => entries.find(forKey(key.equals))?.item;
    
    iterator() => entries.iterator();
    
    equals(Object that) => (super of Map<Key,Item>).equals(that);
    hash => (super of Map<Key,Item>).hash;
    
}

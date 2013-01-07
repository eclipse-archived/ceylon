doc "A Map implementation that wraps an `Iterable` of 
     entries. All operations, such as lookups, size, etc. 
     are performed on the `Iterable`."
by "Enrique Zamudio"
shared class LazyMap<out Key,out Item>({Key->Item...} entries)
    satisfies Map<Key,Item>
        given Key satisfies Object
        given Item satisfies Object {
    
    shared actual LazyMap<Key, Item> clone => this;
    
    shared actual Integer size =>
        entries.count((Key->Item e) => true);
    
    shared actual Item? item(Object key) =>
        entries.find((Key->Item e) => e.key == key)?.item;
    
    shared actual Iterator<Key->Item> iterator =>
        entries.iterator;
    
    shared actual default Boolean equals(Object that) {
        if (is Map<Object,Object> that) {
            if (that.size==size) {
                for (entry in this) {
                    if (exists item = that[entry.key]) {
                        if (item==entry.item) {
                            continue;
                        }
                    }
                    return false;
                }
                else {
                    return true;
                }
            }
        }
        return false;
    }
    
    shared actual default Integer hash {
        variable Integer hashCode = 1;
        for(elem in entries) {
            hashCode *= 31;
            hashCode += elem.hash;
        }
        return hashCode;
    }
    
}

shared interface Map<out Key,out Item> 
        satisfies Collection<Key->Item> &
                  Correspondence<Equality, Item> & 
                  Cloneable<Map<Key,Item>>
        given Key satisfies Equality 
        given Item satisfies Equality {
    
    shared actual default Integer count(Equality element) {
        if (is Key->Item element) {
            if (exists item = item(element.key)) {
                return item==element.item then 1 else 0;
            }
            else {
                return 0;
            }
        }
        else {
            return 0;
        }
    }
    
    shared actual default Boolean equals(Equality that) {
        if (is Map<Equality,Equality> that) {
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
    
    actual shared default Set<Key> keys {
        object keySet satisfies Set<Key> {
            shared actual Set<Key> clone { 
                return this;
            }
            shared actual Boolean equals(Equality that) {
                return false;
            }
            shared actual Integer hash {
                return 0;
            }
            shared actual Iterator<Key> iterator {
                return bottom;
            }
            shared actual Integer size {
                return outer.size;
            }
            shared actual String string {
                return "";
            }
            shared actual Set<Equality> and(Set<Equality> bits) {
                return bottom;
            }
            shared actual Set<Equality> or(Set<Equality> bits) {
                return bottom;
            }
            shared actual Set<Equality> xor(Set<Equality> bits) {
                return bottom;
            }
            shared actual Set<Equality> complementIn(Set<Equality> bits) {
                return bottom;
            }        
        }
        return keySet;
    }
    
    shared default Collection<Item> values {
        object valueCollection satisfies Collection<Item> {
            shared actual Collection<Item> clone {
                return this;
            }
            shared actual Boolean equals(Equality that) {
                return bottom;
            }
            shared actual Integer hash { 
                return 0;
            }
            shared actual Iterator<Item> iterator {
                return bottom;
            }
            shared actual Integer size { 
                return outer.size;
            }
            shared actual String string { 
                return "";
            }
        
        }
        return valueCollection;
    }
    
    shared default Map<Item, Set<Key>> inverse {
        throw; //TODO!
    }
}
shared interface Correspondence<in Key, out Item>
        given Key satisfies Equality {
    
    doc "Binary lookup operator x[key]. Returns the value defined
         for the given key, or |null| if there is no value defined
         for the given key."
    shared formal Item? item(Key key);
    
    shared default Boolean defines(Key key) {
        return item(key) exists;
    }
    
    shared default Category keys {
        object keys satisfies Category {
            shared actual Boolean contains(Object key) {
                if (is Key key) {
                    return defines(key);
                }
                else {
                    return false;
                }
            }
        }
        return keys;
    }
    
    shared default Boolean definesEvery(Key... keys) {
        for (Key key in keys) {
            if (!defines(key)) {
                return false;
            }
        }
        fail {
            return true;
        }
    }
    
    shared default Boolean definesAny(Key... keys) {
        for (Key key in keys) {
            if (defines(key)) {
                return true;
            }
        }
        fail {
            return false;
        }
    }
    
    shared default Item?[] items(Key... keys) {
        if (nonempty keys) {
            return Entries(keys.clone);
        }
        else {
            return {};
        }
    }
        
    class Entries(Sequence<Key> keys)
            extends Object()
            satisfies Sequence<Item?> {
        shared actual Natural lastIndex {
            return keys.lastIndex;
        }
        shared actual Item? first {
            return outer.item(keys.first);
        }
        shared actual Item?[] rest {
            return outer.items(keys.rest...);
        }
        shared actual Item? item(Natural index) {
            if (exists Key key = keys.item(index)) {
                return outer.item(key);
            }
            else {
                return null;
            }
        }
        shared actual String string {
            //fake impl
            variable String result := "{ ";
            for (Key key in keys) {
                Item? element = outer.item(key);
                if (is Object element) {
                    result += element.string + ", ";
                }
            }
            result += " }";
            return result;
        }
        shared actual Sequence<Item?> clone {
            return this;
        }
    }
    
}
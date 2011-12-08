doc "Abstract supertype of objects which associate values 
     with keys. `Correspondence` does not satisfy `Category`,
     since in some cases, for examples lists, it is 
     convenient to consider the subtype a `Category` of its
     values, and in other cases, for example maps, it is
     convenient to treat the subtype as a `Category` of its
     entries."
by "Gavin"
shared interface Correspondence<in Key, out Item>
        given Key satisfies Equality {
    
    doc "Returns the value defined for the given key, or 
         `null` if there is no value defined for the given 
         key."
    see (items)
    shared formal Item? item(Key key);
    
    doc "Determines if there is a value defined for the 
         given key."
    see (definesAny, definesEvery, keys)
    shared default Boolean defines(Key key) {
        return exists item(key);
    }
    
    doc "The `Category` of all keys for which a value is 
         defined by this `Correspondence`."
    see (defines)
    shared default Category keys {
        object keys satisfies Category {
            shared actual Boolean contains(Equality key) {
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
    
    doc "Determines if this `Correspondence` defines a value
         for every one of the given keys."
    see (defines)
    shared default Boolean definesEvery(Key... keys) {
        for (Key key in keys) {
            if (!defines(key)) {
                return false;
            }
        }
        else {
            return true;
        }
    }
    
    doc "Determines if this `Correspondence` defines a value
         for any one of the given keys."
    see (defines)
    shared default Boolean definesAny(Key... keys) {
        for (Key key in keys) {
            if (defines(key)) {
                return true;
            }
        }
        else {
            return false;
        }
    }
    
    doc "Returns the items defined for the given keys, in
         the same order as the corresponding keys."
    see (item)
    shared default Item?[] items(Key... keys) {
        if (nonempty keys) {
            return Items(keys.clone);
        }
        else {
            return {};
        }
    }

    class Items(Sequence<Key> keys)
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
        shared actual Item?[] segment(Natural from, 
                                     Natural length) {
            if (nonempty keys = keys.segment(from,length)) {
                return outer.Items(keys);
            }
            else {
                return {};
            }
        }
        shared actual Item?[] span(Natural from, Natural? to) {
            if (nonempty k = keys.span(from,to)) {
                return outer.Items(keys);
            }
            else {
                return {};
            }
        }
        //shared actual String string { return "Entries"; }
        shared actual Sequence<Item?> clone {
            return this;
        }
    }
    
}

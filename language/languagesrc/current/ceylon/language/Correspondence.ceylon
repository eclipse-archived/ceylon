shared interface Correspondence<in Key, out Value>
        given Key satisfies Equality {
    
    doc "Binary lookup operator x[key]. Returns the value defined
         for the given key, or |null| if there is no value defined
         for the given key."
    shared formal Value? value(Key key);
    
    shared default Boolean defines(Key... keys) {
        for (Key key in keys) {
            if (!(value(key) exists)) {
                return false;
            }
        }
        fail {
            return true;
        }
    }
    
    shared default Value?[] values(Key... keys) {
        if (nonempty keys) {
            return Values(keys.clone);
        }
        else {
            return {};
        }
    }
        
    class Values(Sequence<Key> keys)
            extends Object()
            satisfies Sequence<Value?> {
        shared actual Natural lastIndex {
            return keys.lastIndex;
        }
        shared actual Value? first {
            return outer.value(keys.first);
        }
        shared actual Value?[] rest {
            return outer.values(keys.rest);
        }
        shared actual Value? value(Natural index) {
            if (exists Key key = keys.value(index)) {
                return outer.value(key);
            }
            else {
                return null;
            }
        }
        shared actual Sequence<Value?> clone {
            return this;
        }
    }
    
}
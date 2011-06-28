shared interface Correspondence<in Key, out Element>
        given Key satisfies Equality {
    
    doc "Binary lookup operator x[key]. Returns the value defined
         for the given key, or |null| if there is no value defined
         for the given key."
    shared formal Element? element(Key key);
    
    shared default Boolean defines(Key key) {
        return element(key) exists;
    }
    
    shared default object keys satisfies Category {
        shared actual Boolean contains(Object element) {
            if (is Key element) {
                return defines(element);
            }
            else {
                return false;
            }
        }
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
    
    shared default Element?[] elements(Key... keys) {
        if (nonempty keys) {
            return Entries(keys.clone);
        }
        else {
            return {};
        }
    }
        
    class Entries(Sequence<Key> keys)
            extends Object()
            satisfies Sequence<Element?> {
        shared actual Natural lastIndex {
            return keys.lastIndex;
        }
        shared actual Element? first {
            return outer.element(keys.first);
        }
        shared actual Element?[] rest {
            return outer.elements(keys.rest);
        }
        shared actual Element? element(Natural index) {
            if (exists Key key = keys.element(index)) {
                return outer.element(key);
            }
            else {
                return null;
            }
        }
        shared actual Sequence<Element?> clone {
            return this;
        }
    }
    
}
class Bug6068<out Key, out Item>() 
        satisfies Map<Key, Item> 
        given Key satisfies Object {
    shared actual Map<Key,Item> clone() => nothing;
    
    shared actual Boolean defines(Object key) => nothing;
    
    shared actual Item? get(Object key) => nothing;
    
    shared actual Iterator<Key->Item> iterator() => nothing;
    
    shared actual Integer hash {
        variable value hash = 1;
        return hash;
    }
    
    shared actual Boolean equals(Object that) {
        if (is Bug6068<Key,Item> that) {
            return true;
        }
        else {
            return false;
        }
    }
}
class Bug6068Sub<out Key, out Item>() 
        extends Bug6068<Key, Item>()
        given Key satisfies Object {
    
}
interface Bug6068MapSub<out Key, out Item> 
        satisfies Map<Key, Item> 
        given Key satisfies Object {
    
}
class Bug6068Indirect<out Key, out Item>()
        satisfies Bug6068MapSub<Key, Item> 
        given Key satisfies Object {
    shared actual Map<Key,Item> clone() => nothing;
    
    shared actual Boolean defines(Object key) => nothing;
    
    shared actual Item? get(Object key) => nothing;
    
    shared actual Iterator<Key->Item> iterator() => nothing;
    
    shared actual Integer hash {
        variable value hash = 1;
        return hash;
    }
    
    shared actual Boolean equals(Object that) {
        if (is Bug6068<Key,Item> that) {
            return true;
        }
        else {
            return false;
        }
    }
}
shared void bug6068use<Key>() given Key satisfies Object{
    variable Set<Key> ks = Bug6068<Key,Anything>().keys;
    ks = Bug6068Indirect<Key,Anything>().keys;
}
import java.util{
    HashMap,
    JavaIterator=Iterator
}
import ceylon.language.impl{BaseIterable, BaseIterator}

"Need a map-like thing, but can't use java.util.HashMap directly, not ceylon.collection::HashMap"
interface NativeMap<Key,Element> {
    shared formal Element? get(Key id);
    shared formal void put(Key id, Element instanceOrPartial);
    shared formal Boolean contains(Key id);
    shared formal {Key*} keys;
    shared formal {Element*} items;
    shared formal Integer size;
    shared formal actual String string;
}
"Constructor function for a NativeMap"
native NativeMap<Key,Element> nativeMap<Key,Element>();

native("jvm") NativeMap<Key,Element> nativeMap<Key,Element>() {
    return NativeMapImpl<Key,Element>();
}


native("jvm") class NativeMapImpl<Key,Element>() satisfies NativeMap<Key,Element> {
    
    HashMap<Key, Element> map = HashMap<Key, Element>();
    
    shared actual native("jvm") void put(Key id, Element instance) {
        map.put(id, instance);
    }
    
    shared actual native("jvm") Element? get(Key id) {
        return map.get(id of Object?);
    }
    
    shared actual native("jvm") Boolean contains(Key id) {
        return map.containsKey(id of Object?);
    }
    
    shared actual native("jvm") Integer size {
        return map.size();
    }
    
    shared actual native("jvm") Iterable<Element, Null> items {
        return object extends BaseIterable<Element,Null>() satisfies Identifiable {
            
            shared actual Iterator<Element> iterator() {
                JavaIterator<Element> it = outer.map.values().iterator();
                return object extends BaseIterator<Element>() satisfies Identifiable {
                    
                    shared actual Element|Finished next() {
                        if (it.hasNext()) {
                            return it.next();
                        } else { 
                            return finished;
                        }
                    }
                };
            }
        };
    }
    
    
    shared actual native("jvm") Iterable<Key, Null> keys {
        return object extends BaseIterable<Key,Null>() satisfies Identifiable {
            
            shared actual Iterator<Key> iterator() {
                JavaIterator<Key> it = outer.map.keySet().iterator();
                return object extends BaseIterator<Key>() satisfies Identifiable {
                    
                    shared actual Key|Finished next() {
                        if (it.hasNext()) {
                            return it.next();
                        } else { 
                            return finished;
                        }
                    }
                };
            }
        };
    }
    
    shared actual native("jvm") String string {
        return map.string;
    }
}

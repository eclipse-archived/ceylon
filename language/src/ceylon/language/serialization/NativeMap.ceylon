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

/*
 @Ceylon(major=8, minor=0)
 @Class
 @TypeParameters({
    @TypeParameter(value="Key"),
    @TypeParameter(value="Element")
    })
 final class NativeMapImpl<Key,Element> implements NativeMap<Key, Element> {
    private final HashMap<Key, Element> map = new HashMap<Key, Element>();
    private final TypeDescriptor $reified$Key;
    private final TypeDescriptor $reified$Item;
    
    public NativeMapImpl(@Ignore TypeDescriptor $reified$Key, @Ignore TypeDescriptor $reified$Item) {
        this.$reified$Key = $reified$Key ;
        this.$reified$Item= $reified$Item;
    }
    
    public final java.lang.Object put(Key id, Element instance) {
        map.put(id, instance);
        return null;
    }
    
    public final Element get(Key id) {
        return map.get(id);
    }
    
    public final boolean contains(Key id) {
        return map.containsKey(id);
    }
    
    public final long getSize() {
        return map.size();
    }
    
    @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>")
    public final ceylon.language.Iterable<Element, java.lang.Object> getItems() {
        return new BaseIterable<Element,java.lang.Object>($reified$Item, Null.$TypeDescriptor$) {
 
            @Override
            public Iterator<? extends Element> iterator() {
                final java.util.Iterator<Element> it = map.values().iterator();
                return new BaseIterator<Element>($reified$Item) {
 
                    @Override
                    public Object next() {
                        if (it.hasNext()) {
                            return it.next();
                        } else { 
                            return finished_.get_();
                        }
                    }
                    
                };
            }
            
        };
    }
    
    @TypeInfo("ceylon.language::Iterable<Key,ceylon.language::Null>")
    public final ceylon.language.Iterable<Key, java.lang.Object> getKeys() {
        return new BaseIterable<Key,java.lang.Object>($reified$Key, Null.$TypeDescriptor$) {
 
            @Override
            public Iterator<? extends Key> iterator() {
                final java.util.Iterator<Key> it = map.keySet().iterator();
                return new BaseIterator<Key>($reified$Key) {
 
                    @Override
                    public Object next() {
                        if (it.hasNext()) {
                            return it.next();
                        } else { 
                            return finished_.get_();
                        }
                    }
                    
                };
            }
            
        };
    }
    
    public String toString() {
        return map.toString();
    }
 }

 */
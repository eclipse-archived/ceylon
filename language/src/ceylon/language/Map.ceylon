"""Represents a collection which maps _keys_ to _items_,
   where a key can map to at most one item. Each such 
   mapping may be represented by an [[Entry]].
   
   A `Map` is a [[Collection]] of its `Entry`s, and a 
   [[Correspondence]] from keys to items.
   
   The presence of an entry in a map may be tested using the 
   `in` operator:
   
       if ("lang"->"en_AU" in settings) { ... }
 
   The entries of the map may be iterated using `for`:
   
       for (key->item in settings) { ... }
   
   The item for a key may be obtained using the item
   operator:
   
       String lang = settings["lang"] else "en_US";
   
   Keys are compared for equality using [[Object.equals]] or
   [[Comparable.compare]]. There may be at most one entry 
   per key."""
see (`class Entry`, 
     `function forKey`, `function forItem`, 
     `function byItem`, `function byKey`)
shared interface Map<out Key,out Item>
        satisfies Collection<Key->Item> &
                  Correspondence<Object,Item> &
                  Cloneable<Map<Key,Item>>
        given Key satisfies Object
        given Item satisfies Object {
    
    shared actual default Boolean contains(Object entry) {
        if (is Key->Object entry, 
            exists item = get(entry.key)) {
            return item==entry.item;
        }
        else {
            return false;
        }
    }
    
    "Two `Map`s are considered equal iff they have the same 
     _entry sets_. The entry set of a `Map` is the set of 
     `Entry`s belonging to the map. Therefore, the maps are 
     equal iff they have same set of `keys`, and for every 
     key in the key set, the maps have equal items."
    shared actual default Boolean equals(Object that) {
        if (is Map<Object,Object> that,
                that.size==size) {
            for (entry in this) {
                if (exists item = that[entry.key],
                        item==entry.item) {
                    continue;
                }
                else {
                    return false;
                }
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }
    
    shared actual default Integer hash {
        variable Integer hashCode = 1;
        for (elem in this) {
            hashCode *= 31;
            hashCode += elem.hash;
        }
        return hashCode;
    }
    
    "Returns the set of keys contained in this `Map`."
    actual shared default Set<Key> keys {
        value entries = {for (k->v in this) k};
        object keys
                extends LazySet<Key>(entries) {
            contains(Object key) => outer.defines(key);
            size => outer.size;
        }
        return keys;
    }
    
    "Returns all the items stored in this `Map`. An element 
     can be stored under more than one key in the map, and 
     so it can be contained more than once in the resulting 
     collection."
    shared default Collection<Item> values 
            => LazyList { for (k->v in this) v };
    
    "Returns a `Map` in which every key is an `Item` in this 
     map, and every value is the set of keys that stored the 
     `Item` in this map."
    deprecated
    shared default Map<Item,Set<Key>> inverse {
        object inverse
                satisfies Map<Item,Set<Key>> {
            
            clone => this;
            
            shared actual Set<Key>? get(Object item) {
                value keys = getKeys(item);
                return !keys.empty then keys;
            }
            
            Set<Key> getKeys(Object item) 
                    => LazySet { for (k->i in outer) if (item==i) k };
            
            iterator() => UniqueElements { for (k->i in outer) i }
                    .map((Item i)=>i->getKeys(i)).iterator();
            
            equals(Object that) => (super of Map<Item,Set<Key>>).equals(that);
            hash => (super of Map<Item,Set<Key>>).hash;
            
        }
        return inverse;
    }
    
    "Returns a `Map` with the same keys as this map. For
     every key, the item is the result of applying the given 
     transformation function."
    shared default Map<Key,Result> mapItems<Result>(
            "The function that transforms a key/item
             pair, producing the item of the resulting
             map."
            Result mapping(Key key, Item item)) 
            given Result satisfies Object {
        value entries = { for (key->item in this) key->mapping(key,item) };
        object map
                extends LazyMap<Key,Result>(entries) {
            shared actual Result? get(Object key) {
                if (is Key key, exists item=outer[key]) {
                    return mapping(key, item);
                }
                else {
                    return null;
                }
            }
            defines(Object key) => outer.defines(key);
            size => outer.size;
        }
        return map;
    }
            
}

"An immutable [[Map]] with no entries."
shared object emptyMap 
        extends Object() 
        satisfies Map<Nothing, Nothing> {
    
    shared actual Null get(Object key) => null;
    shared actual Set<Nothing> keys => emptySet;
    shared actual Map<Nothing,Nothing> inverse => emptyMap;
    shared actual Collection<Nothing> values => [];
    
    clone => emptyMap;
    iterator() => emptyIterator;
    size => 0;
    empty => true;
    
    defines(Object index) => false;
    
    contains(Object element) => false;
    containsAny({Object*} elements) => false;
    containsEvery({Object*} elements) => false;
    
    shared actual Map<Nothing, Nothing> mapItems<Result>
            (Result mapping(Nothing key, Nothing item))
            given Result satisfies Object 
            => emptyMap;
    
    shared actual Integer count
            (Boolean selecting(Nothing->Nothing element)) 
            => 0;
    
    shared actual [] map<Result>
            (Result collecting(Nothing->Nothing element)) 
            => [];
    
    shared actual Map<Nothing,Nothing> filter
            (Boolean selecting(Nothing->Nothing element)) 
            => emptyMap;
    
    shared actual Result fold<Result>(Result initial,
            Result accumulating(Result partial, Nothing->Nothing element)) 
            => initial;
    
    shared actual Null find
            (Boolean selecting(Nothing->Nothing element)) 
            => null;
    
    shared actual [] collect<Result>
            (Result collecting(Nothing->Nothing element)) 
            => [];
    
    shared actual [] select
            (Boolean selecting(Nothing->Nothing element)) 
            => [];
    
    shared actual Boolean any
            (Boolean selecting(Nothing->Nothing element)) 
            => false;
    
    shared actual Boolean every
            (Boolean selecting(Nothing->Nothing element)) 
            => true;
    
    skipping(Integer skip) => emptyMap;
    taking(Integer take) => emptyMap;
    by(Integer step) => emptyMap;
    
}
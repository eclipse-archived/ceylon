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
                  Correspondence<Object,Item>
        given Key satisfies Object {
    
    "Determines if there is an entry in this map with the
     given [[key]]."
    see (`function contains`)
    shared actual formal Boolean defines(Object key);
    
    "Returns the item of the entry with the given [[key]], 
     or `null` if there is no entry with the given `key` in
     this map."
    shared actual formal Item? get(Object key);
    
    "Determines if the given [[value|entry]] is an [[Entry]]
     belonging to this map."
    see (`function defines`)
    shared actual default Boolean contains(Object entry) {
        if (is Key->Anything entry, defines(entry.key)) {
            if (exists item = get(entry.key)) {
                if (exists entryItem = entry.item) {
                    return item==entryItem;
                }
                else {
                    return false;
                }
            }
            else {
                return !entry.item exists;
            }
        }
        else {
            return false;
        }
    }
    
    shared actual formal Map<Key,Item> clone();
    
    "Two maps are considered equal iff they have the same 
     _entry sets_. The entry set of a `Map` is the set of 
     `Entry`s belonging to the map. Therefore, the maps are 
     equal iff they have same set of `keys`, and for every 
     key in the key set, the maps have equal items."
    shared actual default Boolean equals(Object that) {
        if (is Map<Object,Anything> that,
                that.size==size) {
            for (entry in this) {
                value thatItem = that[entry.key];
                if (exists thisItem = entry.item) {
                    if (exists thatItem) {
                        if (thatItem!=thisItem) {
                            return false;
                        }
                    }
                    else {
                        return false;
                    }
                }
                else if (thatItem exists) {
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
        variable Integer hashCode = 0;
        for (elem in this) {
            hashCode += elem.hash;
        }
        return hashCode;
    }
    
    "A [[Collection]] containing the keys of this map."
    actual shared default Collection<Key> keys {
        object keys
                satisfies Collection<Key> {
            contains(Object key) => outer.defines(key);
            iterator() => { for (k->v in outer) k }.iterator();
            clone() => [*this];
            size => outer.size;
        }
        return keys;
    }
    
    "A [[Collection]] containing the items stored in this 
     map. An element can be stored under more than one key 
     in the map, and so it can occur more than once in the 
     resulting collection."
    shared default Collection<Item> items {
        object items
                satisfies Collection<Item> {
            shared actual Boolean contains(Object item) {
                for (k->v in outer) {
                    if (exists v, v==item) {
                        return true;
                    }
                }
                return false;
            }
            iterator() => { for (k->v in outer) v }.iterator();
            clone() => [*this];
            size => outer.size;
        }
        return items;
    }
    
    "Produces a map with the same [[keys]] as this map. 
     For every key, the item is the result of applying the 
     given [[transformation|Map.mapItems.mapping]] function 
     to its associated item in this map. This is a lazy 
     operation, returning a view of this map."
    shared default Map<Key,Result> mapItems<Result>(
        "The function that transforms a key/item pair of
         this map, producing the item of the resulting map."
        Result mapping(Key key, Item item)) 
            given Result satisfies Object {
        object map
                extends Object()
                satisfies Map<Key,Result> {
            
            defines(Object key) => outer.defines(key);
            
            shared actual Result? get(Object key) {
                if (is Key key, defines(key)) {
                    assert (is Item item = outer[key]);
                    return mapping(key, item);
                }
                else {
                    return null;
                }
            }
            
            function mapEntry(Key->Item entry) 
                    => entry.key -> 
                        mapping(entry.key, entry.item);
            
            iterator() => outer.map(mapEntry).iterator();
            
            size => outer.size;
            
            clone() => outer.clone().mapItems(mapping);
            
        }
        return map;
    }
    
    "Produces a map by applying a [[filtering]] function 
     to the [[keys]] of this map. This is a lazy operation, 
     returning a view of this map."
    shared default Map<Key,Item> filterKeys(
        "The predicate function that filters the keys of 
         this map, determining if there is a corresponding
         entry in the resulting map."
        Boolean filtering(Key key)) {
        object map
                extends Object()
                satisfies Map<Key,Item> {
            
            shared actual Item? get(Object key) {
                if (is Key key, filtering(key)) {
                    return outer[key];
                }
                else {
                    return null;
                }
            }
            
            shared actual Boolean defines(Object key) {
                if (is Key key, filtering(key)) {
                    return outer.defines(key);
                }
                else {
                    return false;
                }
            }
            
            function filterEntry(Key->Item entry) 
                    => filtering(entry.key);
            
            iterator() => outer.filter(filterEntry).iterator();
            
            clone() => outer.clone().filterKeys(filtering);
            
        }
        return map;
    }
    
    "Produces a map whose keys are the union of the keys
     of this map, with the keys of the given [[map|other]].
     For any given key in the resulting map, its associated
     item is the item associated with the key in the given
     map, if any, or the item associated with the key in 
     this map otherwise.
     
     That is, for any `key` in the resulting patched map:
     
         map.patch(other)[key] == other[key] else map[key]
     
     This is a lazy operation producing a view of this map
     and the given map."
    shared default
    Map<Key|OtherKey,Item|OtherItem> patch<OtherKey,OtherItem>
            (Map<OtherKey,OtherItem> other) 
            given OtherKey satisfies Object 
            given OtherItem satisfies Object {
        object patch 
                extends Object()
                satisfies Map<Key|OtherKey,Item|OtherItem> {
            
            get(Object key) => other[key] else outer[key];
            
            clone() => outer.clone().patch(other.clone());
            
            defines(Object key) 
                    => other.defines(key) || 
                       outer.defines(key);
            
            shared actual Boolean contains(Object entry) {
                if (is Entry<Object,Object> entry) {
                    return entry in other || 
                            !other.defines(entry.key) 
                                && entry in outer;
                }
                else {
                    return false;
                }
            }
            
            //efficient when map is much smaller than outer,
            //which is probably the common case 
            size => outer.size +
                    other.keys.count(not(outer.defines));
            
            iterator() => ChainedIterator(other,
                outer.filter(not(other.contains)));
            
        }
        return patch;
    }
    
}

"An immutable [[Map]] with no entries."
shared object emptyMap 
        extends Object() 
        satisfies Map<Nothing, Nothing> {
    
    shared actual Null get(Object key) => null;
    shared actual Collection<Nothing> keys
            => emptySet;
    shared actual Collection<Nothing> items
            => emptySet;
    
    clone() => this;
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
    
    shared actual Null find
            (Boolean selecting(Nothing->Nothing element)) 
            => null;
    
    shared actual Null findLast
            (Boolean selecting(Nothing->Nothing element)) 
            => null;
    
    shared actual Boolean any
            (Boolean selecting(Nothing->Nothing element)) 
            => false;
    
    shared actual Boolean every
            (Boolean selecting(Nothing->Nothing element)) 
            => true;
    
    skip(Integer skipping) => this;
    take(Integer taking) => this;
    by(Integer step) => this;
    
}

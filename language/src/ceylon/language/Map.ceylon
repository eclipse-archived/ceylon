doc "Represents a collection which maps keys to items,
     where a key can map to at most one item. Each such 
     mapping may be represented by an `Entry`.
     
     A `Map` is a `Collection` of its `Entry`s, and a 
     `Correspondence` from keys to items."
see (forKey, forItem, byItem, byKey)
shared interface Map<out Key,out Item>
        satisfies Collection<Key->Item> &
                  Correspondence<Object,Item> &
                  Cloneable<Map<Key,Item>>
        given Key satisfies Object
        given Item satisfies Object {

    doc "Two `Map`s are considered equal iff they have the 
         same _entry sets_. The entry set of a `Map` is the
         set of `Entry`s belonging to the map. Therefore, the
         maps are equal iff they have same set of `keys`, and 
         for every key in the key set, the maps have equal
         items."
    shared actual default Boolean equals(Object that) {
        if (is Map<Object,Object> that) {
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

    shared actual default Integer hash {
        variable Integer hashCode := 1;
        for(Entry<Key,Item> elem in this){
            hashCode *= 31;
            hashCode += elem.hash;
        }
        return hashCode;
    }

    doc "Returns the set of keys contained in this `Map`."
    actual shared default Set<Key> keys {
        object keySet satisfies Set<Key> {
            shared actual Set<Key> clone {
                return this;
            }
            shared actual Boolean equals(Object that) {
                return false;
            }
            shared actual Integer hash {
                return outer.size;
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
            shared actual Set<Key> complement<Other>(Set<Other> set)
                    given Other satisfies Object {
                return bottom;
            }
            shared actual Set<Key|Other> exclusiveUnion<Other>(Set<Other> set)
                    given Other satisfies Object {
                return bottom;
            }
            shared actual Set<Key&Other> intersection<Other>(Set<Other> set)
                    given Other satisfies Object {
                return bottom;
            }
            shared actual Set<Key|Other> union<Other>(Set<Other> set)
                    given Other satisfies Object {
                return bottom;
            }
        }
        return keySet;
    }

    doc "Returns all the values stored in this `Map`. An 
         element can be stored under more than one key in 
         the map, and so it can be contained more than once 
         in the resulting collection."
    shared default Collection<Item> values {
        object valueCollection satisfies Collection<Item> {
            shared actual Collection<Item> clone {
                return this;
            }
            shared actual Boolean equals(Object that) {
                return false;
            }
            shared actual Integer hash {
                return outer.size;
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

    doc "Returns a `Map` in which every key is an `Item` in 
         this map, and every value is the set of keys that 
         stored the `Item` in this map."
    shared default Map<Item, Set<Key>> inverse {
        object inverse satisfies Map<Item, Set<Key>> {
            shared actual Map<Item,Set<Key>> clone {
                return this;
            }
            shared actual Boolean equals(Object that) {
                return false;
            }
            shared actual Integer hash {
                return outer.size;
            }
            shared actual Set<Key>? item(Object key) {
                return bottom;
            }
            shared actual Iterator<Entry<Item,Set<Key>>> iterator {
                return bottom;
            }
            shared actual Integer size {
                return outer.size;
            }
            shared actual String string {
                return "";
            }
        }
        return inverse;
    }

    shared default Map<Key,Result> mapItems<Result>(Result mapping(Key key, Item item)) 
            given Result satisfies Object {
        object mapped extends Object() 
                satisfies Map<Key, Result> {
            shared actual Map<Key, Result> clone { return this; } //TODO: should take a copy
            shared actual Result? item(Object key) {
                if (is Key key) {
                    if (exists item=outer[key]) {
                        return mapping(key, item);
                    }
                }
                return null;
            }
            shared actual Iterator<Key->Result> iterator {
                return elements { for (key->item in outer) key->mapping(key,item) }.iterator;
            }
            shared actual Integer size { return outer.size; }
            shared actual String string { return "mapItems"; } //TODO
        }
        return mapped;
    }
}

shared Map<Key, Item> map<Key, Item>(Entry<Key,Item>... entries)
        given Key satisfies Object
        given Item satisfies Object {
    throw;
}

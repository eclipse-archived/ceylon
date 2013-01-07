doc "Represents a collection which maps _keys_ to _items_,
     where a key can map to at most one item. Each such 
     mapping may be represented by an `Entry`.
     
     A `Map` is a `Collection` of its `Entry`s, and a 
     `Correspondence` from keys to items.

     The prescence of an entry in a map may be tested
     using the `in` operator:
     
         if (\"lang\"->\"en_AU\" in settings) { ... }
     
     The entries of the map may be iterated using `for`:
     
         for (key->item in settings) { ... }
     
     The item for a key may be obtained using the item
     operator:
     
         String lang = settings[\"lang\"] else \"en_US\";"
see (Entry, forKey, forItem, byItem, byKey)
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
    
    doc "Returns the set of keys contained in this `Map`."
    actual shared default Set<Key> keys =>
            LazySet({for (k->v in this) k});
    
    doc "Returns all the values stored in this `Map`. An 
         element can be stored under more than one key in 
         the map, and so it can be contained more than once 
         in the resulting collection."
    shared default Collection<Item> values =>
            LazyList({for (k->v in this) v});
    
    doc "Returns a `Map` in which every key is an `Item` in 
         this map, and every value is the set of keys that 
         stored the `Item` in this map."
    shared default Map<Item, Set<Key>> inverse {
        object inverse 
                extends Object() 
                satisfies Map<Item, Set<Key>> {
            
            shared actual Map<Item,Set<Key>> clone => this;
            
            shared actual Set<Key>? item(Object key) => 
                    LazySet({for (k->v in outer) if (v==key) k});
            
            shared actual Iterator<Item->Set<Key>> iterator =>
                    outer.values.map((Item e) => e ->
                            LazySet({for (k->v in outer) if (v==e) k}))
                                    .iterator;
            
            shared actual Integer size => outer.size;
            
        }
        return inverse;
    }
    
    doc "Returns a `Map` with the same keys as this map. For
         every key, the item is the result of applying the
         given transformation function."
    shared default Map<Key,Result> mapItems<Result>(
            doc "The function that transforms a key/item
                 pair, producing the item of the resulting
                 map."
            Result mapping(Key key, Item item)) 
            given Result satisfies Object {
        object mapped extends Object() 
                satisfies Map<Key, Result> {
            shared actual Map<Key, Result> clone {
                //TODO: should take a copy 
                return this;
            }
            shared actual Result? item(Object key) {
                throw;
                //if (is Key key) {
                //    if (exists item=outer[key]) {
                //        return mapping(key, item);
                //    }
                //}
                //return null;
            }
            shared actual Iterator<Key->Result> iterator {
                return outer.map((Key->Item e) =>
                        e.key -> mapping(e.key,e.item))
                                .iterator;
            }
            shared actual Integer size { 
                return outer.size; 
            }
        }
        return mapped;
    }
    
}

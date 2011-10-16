doc "A finite mapping from keys to values which satisfy 
     Equality. A Map may not contain the same key twice. A
     Map may contain the same value more than once, at 
     different keys."
by "Gavin"
see (OrderedMap)
shared interface Map<Key, out Item> 
        satisfies Correspondence<Key, Item> & 
                  Set<Entry<Key,Item>> &
                  Cloneable<Map<Key,Item>>
        given Key satisfies Equality
        given Item satisfies Equality {

    doc "The set of keys for which a value is defined."
    shared actual formal Set<Key> keys;

    doc "The collection of values."
    shared formal Collection<Item> values;

}


doc "A map whose entries have a well-defined order."
by "Gavin"
shared interface OrderedMap<Key, out Item>
        satisfies Map<Key, Item> & OrderedSet<Entry<Key, Item>> 
        given Key satisfies Equality
        given Item satisfies Equality {}
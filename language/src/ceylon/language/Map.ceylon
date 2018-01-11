/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"""A collection which maps _keys_ to _items_, where a key 
   can map to at most one item. Each such mapping may be 
   represented by an [[Entry]]. Thus, each distinct key 
   occurs in at most one entry. Two 
   non-[[identical|Identifiable]] keys are considered 
   distinct only if they are unequal, according to their own 
   definition of [[value equality|Object.equals]].
   
   A `Map` is a [[Collection]] of its `Entry`s, and a 
   [[Correspondence]] from keys to items.
   
   A new `Map` may be obtained by calling the function 
   [[ceylon.language::map]].
   
       value settings = map { "lang"->"en_AU", "loc"->"ES" };
   
   The [[emptyMap]] is a `Map` with no entries.
   
   The presence of an entry in a map may be tested using the 
   `in` operator:
   
       if ("lang"->"en_AU" in settings) { ... }
 
   The entries of the map may be iterated using `for`:
   
       for (key->item in settings) { ... }
   
   The item for a key may be obtained using the item
   operator:
   
       String lang = settings["lang"] else "en_US";
   
   An implementation of `Map` may compare keys for equality 
   using [[Object.equals]] or [[Comparable.compare]]."""
see (class Entry, function package.map,
     function forKey, function forItem, 
     function byItem, function byKey,
     value emptyMap)
tagged("Collections")
shared interface Map<out Key=Object, out Item=Anything>
        satisfies Collection<Key->Item> 
                & Correspondence<Object,Item>
        given Key satisfies Object {
    
    "Returns the item of the entry with the given [[key]], 
     or `null` if there is no entry with the given `key` in
     this map."
    see (function getOrDefault)
    shared actual formal Item? get(Object key);
    
    "Determines if there is an entry in this map with the
     given [[key]]."
    see (function contains)
    shared actual formal Boolean defines(Object key);
    
    "Returns the item of the entry with the given [[key]], 
     or the given [[default]] if there is no entry with the 
     given `key` in this map.
     
     For maps with non-null items, the expression:
     
         map.getOrDefault(key, def)
     
     is equivalent to this common idiom:
     
         map[key] else def
     
     However, when the map has null items, `getOrDefault()`
     will preserve them.
     
     Note that high-quality implementations of `Map` should 
     refine this default implementation."
    see (function get)
    since("1.2.0")
    shared default Item|Default getOrDefault<Default>
            (Object key, Default default) {
        if (defines(key)) {
            if (exists item = get(key)) {
                return item;
            }
            else {
                assert (is Item null);
                return null;
            }
        }
        else {
            return default;
        }
    }
    
    function lookup(Object key)
            => getOrDefault(key, Missing.instance);
    
    "Determines if the given [[value|entry]] is an [[Entry]]
     belonging to this map."
    see (function defines)
    shared actual default Boolean contains(Object entry) {
        if (is Object->Anything entry) {
            let (key -> it = entry);
            if (defines(key)) {
                value item = get(key);
                return 
                    if (exists it, exists item)
                    then item == it
                    else it exists == item exists;
            }
        }
        return false;
    }
    
    distinct => this;
    
    shared actual {<Key->Item>*} 
    defaultNullElements<Default>(Default defaultValue)
            given Default satisfies Object => this;
    
    "A shallow copy of this map, that is, a map with the
     same entries as this map, which do not change if the
     entries in this map change."
    shared actual default Map<Key,Item> clone() 
            => package.map(this);
    
    "A [[Set]] containing the keys of this map."
    shared actual default Set<Key> keys
            => object extends Object() 
                      satisfies Set<Key> {
        contains(Object key) => outer.defines(key);
        iterator() => outer.map(Entry.key).iterator();
        size => outer.size;
        empty => outer.empty;
        clone() => set(this);
    };
    
    "A [[Collection]] containing the items stored in this 
     map. An element can be stored under more than one key 
     in the map, and so it can occur more than once in the 
     resulting collection."
    since("1.1.0")
    shared default Collection<Item> items => Items();
    
    "A bag of items."
    class Items() extends Object() 
                  satisfies Collection<Item> {
        contains(Object item) 
                => outer.any((_->it) 
                    => if (exists it) then it==item 
                            else false);
        iterator() => outer.map(Entry.item).iterator();
        size => outer.size;
        empty => outer.empty;
        clone() => [*this];
        //implement hash and equals for bag semantics
        hash => frequencies().hash;
        equals(Object that) 
                => if (is Items that) 
                then frequencies()==that.frequencies() 
                else false;
    }
    
    "Invert this map, producing a new immutable map where 
     the keys of the new map are the non-null items of this
     map, and each item of the new map is a nonempty 
     sequence of keys of this map.
     
     For example, the expression:
     
         { \"fee\", \"fi\", \"fo\", \"fum\", \"foo\" }
            .tabulate(String.size)
            .inverse()
     
     produces the map 
     `{ 2->[\"fo\", \"fi\"], 3->[ \"fum\", \"fee\", \"foo\"] }`.
     
     The order of keys in the key sequences is not defined
     and should not be relied upon.
     
     This is an eager operation, and the resulting map does
     not reflect changes to this map."
    since("1.2.0")
    shared default Map<Item&Object, [Key+]> inverse() 
            => coalescedMap
                .summarize<Item&Object,ElementEntry<Key>>
                    (Entry.item, (keys, key->_) 
                        => ElementEntry(keys, key))
                //not very useful, since the entries of a
                //map don't usually have a very meaningful
                //order (except for TreeMaps!)
                //but we do need to convert it to a Sequence
                .mapItems((_, item) => item.reversedSequence());
    
    "Two maps are considered equal iff they have the same 
     _entry sets_. The entry set of a `Map` is the set of 
     `Entry`s belonging to the map. Therefore, the maps are 
     equal iff they have same set of `keys`, and for every 
     key in the key set, the maps have equal items."
    shared actual default Boolean equals(Object that) {
        if (is Map<> that, that.size==size) {
            for (key -> thisItem in this) {
                value thatItem = that[key];
                if (exists thisItem, exists thatItem) {
                    if (thisItem!=thatItem) {
                        return false;
                    }
                }
                else if (thisItem exists!=thatItem exists) {
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
        variable value hashCode = 0;
        for (elem in this) {
            hashCode += elem.hash;
        }
        return hashCode;
    }
    
    "Produces a map with the same [[keys]] as this map. For 
     every key, the item is the result of applying the given 
     [[transformation|Map.mapItems.mapping]] function to its 
     associated item in this map. This is a lazy operation, 
     returning a view of this map."
    shared default 
    Map<Key,Result> mapItems<Result>(
        "The function that transforms a key/item pair of
         this map, producing the item of the resulting map."
        Result mapping(Key key, Item item)) 
            => object
            extends Object()
            satisfies Map<Key,Result> {
        
        defines(Object key) => outer.defines(key);
        
        shared actual Result? get(Object key) { 
            if (is Key key) {
                return
                    switch (item = outer.lookup(key))
                    case (Missing.instance) null
                    else mapping(key, item);
            }
            else {
                return null;
            } 
        }
        
        shared actual Result|Default
        getOrDefault<Default>
                (Object key, Default default) {
            if (is Key key) {
                return
                    switch (item = outer.lookup(key))
                    case (Missing.instance) default 
                    else mapping(key, item);
            }
            else {
                return default;
            }
        }
        
        function mapEntry(Key->Item entry) 
                => entry.key
                -> mapping(entry.key, entry.item);
        
        iterator() => outer.map(mapEntry).iterator();
        
        size => outer.size;
        
        keys => outer.keys;
        
        clone() => outer.clone().mapItems(mapping);
        
    };
    
    "Produces a map containing the elements of this map, 
     after replacing every `null` item in the map with the 
     [[given default value|defaultValue]]. The item `null` 
     does not ocur in the resulting map."
    see (value coalescedMap)
    since("1.2.0")
    shared default
    Map<Key,Item&Object|Default>
    defaultNullItems<Default>(
        "A default value that replaces `null` items."
        Default defaultValue)
            given Default satisfies Object
            => mapItems((key, elem) 
                => elem else defaultValue);
    
    "Produces a map by applying a [[filtering]] function 
     to the [[keys]] of this map. This is a lazy operation, 
     returning a view of this map."
    since("1.1.0")
    shared default 
    Map<Key,Item> filterKeys(
        "The predicate function that filters the keys of 
         this map, determining if there is a corresponding
         entry in the resulting map."
        Boolean filtering(Key key))
            => object
            extends Object()
            satisfies Map<Key,Item> {
        
        get(Object key)
                => if (is Key key, filtering(key))
                then outer[key] 
                else null;
        
        defines(Object key) 
                => if (is Key key, filtering(key))
                then outer.defines(key) 
                else false;
        
        shared actual Item|Default
        getOrDefault<Default>
                (Object key, Default default) 
                => if (is Key key, filtering(key))
                then outer.getOrDefault(key, default)
                else default;
        
        iterator() 
                => outer.filter(forKey(filtering))
                        .iterator();
        
        clone() => outer.clone().filterKeys(filtering);
        
    };
    
    "Produces a map whose keys are the union of the keys
     of this map, with the keys of the given [[map|other]].
     For any given key in the resulting map, its associated
     item is the item associated with the key in the given
     map, if any, or the item associated with the key in 
     this map otherwise.
     
     That is, for any `key` in the resulting patched map:
     
         map.patch(other)[key] == other.getOrDefault(key, map[key])
     
     This is a lazy operation producing a view of this map
     and the given map."
    since("1.1.0")
    shared default
    Map<Key|OtherKey,Item|OtherItem> 
    patch<OtherKey,OtherItem>
            (Map<OtherKey,OtherItem> other) 
            given OtherKey satisfies Object
            => object 
            extends Object()
            satisfies Map<Key|OtherKey,Item|OtherItem> {
        
        defines(Object key) 
                => other.defines(key) || 
                   outer.defines(key);
        
        get(Object key) 
                => switch (result = other.lookup(key))
                case (Missing.instance) outer.get(key) 
                else result;
        
        shared actual OtherItem|Item|Default 
        getOrDefault<Default>
                (Object key, Default default) 
                => switch (result = other.lookup(key))
                case (Missing.instance)
                    outer.getOrDefault(key, default)
                else result;
        
        clone() => outer.clone().patch(other.clone());
        
        contains(Object entry)
                => if (is Object->Anything entry)
                then entry in other ||
                        !other.defines(entry.key)
                            && entry in outer
                else false;
        
        //efficient when map is much smaller than outer,
        //which is probably the common case 
        size => outer.size +
                other.keys.count(not(outer.defines));
        
        iterator()
                => ChainedIterator(other,
                        outer.filter((key -> _) 
                            => !other.defines(key)));
        
    };
    
    "A map with every entry of this map whose item is
     non-null."
    see (function defaultNullItems)
    since("1.2.0")
    shared default
    Map<Key,Item&Object> coalescedMap 
            => object
            extends Object()
            satisfies Map<Key,Item&Object> {
        
        defines(Object key) => outer[key] exists;
        
        get(Object key) => outer[key] of <Item&Object>?;
        
        shared actual Default|Item&Object 
        getOrDefault<Default>
                (Object key, Default default)
                => outer.getOrDefault(key, default) 
                    else default;
        
        iterator()
                => { for (entry in outer) 
                     if (exists it = entry.item) 
                            entry.key -> it }
                        .iterator();
        
        clone() => outer.clone().coalescedMap;
        
    };
    
}

"Create a new immutable [[Map]] containing every [[Entry]] 
 produced by the given [[stream]], resolving items with
 duplicate keys according to the given [[function|choosing]].
 
 For example:
 
     map { 1->\"hello\", 2->\"goodbye\" }
 
 produces the map `{ 1->\"hello\", 2->\"goodbye\" }`.
 
 This is an eager operation and the resulting map does not 
 reflect changes to the given [[stream]]."
since("1.2.0")
shared Map<Key,Item> map<Key,Item>(
    "The stream of entries."
    {<Key->Item>*} stream,
    "A function that chooses between items with duplicate 
     keys. By default, the item that occurs _earlier_ in the 
     stream is chosen."
    Item choosing(Item earlier, Item later) => earlier)
        given Key satisfies Object
        => stream.summarize(Entry.key, 
                (Item? item, entry) 
                        => if (exists item) 
                        then choosing(item, entry.item)
                        else entry.item);

"An immutable [[Map]] with no entries."
tagged("Collections")
shared object emptyMap 
        extends Object() 
        satisfies Map<Nothing, Nothing> {
    
    get(Object key) => null;
    
    shared actual Default getOrDefault<Default>
            (Object key, Default default) => default;
    
    keys => emptySet;
    items => emptySet;
    
    clone() => this;
    iterator() => emptyIterator;
    size => 0;
    empty => true;
    
    defines(Object index) => false;
    
    contains(Object element) => false;
    containsAny({Object*} elements) => false;
    containsEvery({Object*} elements) => elements.empty;
    
    shared actual 
    Map<Nothing, Nothing> mapItems<Result>
            (Result mapping(Nothing key, Nothing item))
            => emptyMap;
    
    count(Boolean selecting(Nothing->Nothing element)) => 0;
    any(Boolean selecting(Nothing->Nothing element)) => false;
    every(Boolean selecting(Nothing->Nothing element)) => true;
    
    shared actual 
    Null find(Boolean selecting(Nothing->Nothing element)) 
            => null;
    
    shared actual 
    Null findLast(Boolean selecting(Nothing->Nothing element)) 
            => null;
        
    skip(Integer skipping) => this;
    take(Integer taking) => this;
    by(Integer step) => this;
    
    shared actual 
    void each(void step(Nothing->Nothing element)) {}
    
}

class Missing of instance {
    shared new instance {}
}

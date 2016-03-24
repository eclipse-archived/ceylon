"Abstract supertype of objects which associate values with 
 keys. A `Correspondence<Key,Item>` may be a viewed as a 
 partial function from domain `Key` to range `Item`, where
 some `Key`s have no `Item`.
 
 `Correspondence` does not satisfy [[Category]], since in 
 some cases&mdash;`List`, for example&mdash;it is convenient 
 to consider the subtype a `Category` of its indexed items, 
 and in other cases&mdash;`Map`, for example&mdash;it is 
 convenient to treat the subtype as a `Category` of its 
 [[entries|Entry]].
 
 The item corresponding to a given key may be obtained from 
 a `Correspondence` using the item operator:
 
     value bg = settings[\"backgroundColor\"] else white;
 
 The `get()` operation and item operator result in an
 optional type, to reflect the possibility that there may be
 no item for the given key."
see (`interface Map`, 
     `interface List`, 
     `interface Category`)
by ("Gavin")
tagged("Collections")
shared interface Correspondence<in Key, out Item=Anything>
        given Key satisfies Object {
    
    "Returns the value defined for the given key, or `null` 
     if there is no value defined for the given key."
    see (`function Correspondence.getAll`)
    shared formal Item? get(Key key);
    
    "Determines if there is a value defined for the given 
     key."
    see (`function Correspondence.definesAny`, 
         `function Correspondence.definesEvery`, 
         `value Correspondence.keys`)
    shared formal Boolean defines(Key key);
    
    /*"Return a boolean value indicating whether there is an
     item with the given [[key]], together with the 
     [[item|Item]] for that key, if any.
     
         value found -> item = map.lookup(key);
         if (found) {
             print(item);
         }
     
     This operation is useful for correspondences which 
     contain [[null]] items, where a null return value for 
     [[get]] does not distinguish between:
     
     - the case that there is no item for the given key, and
     - the case that `null` is the item for the given key."
    shared default
    see (`function defines`, `function get`)
    Boolean->Item? lookup(Key key)
            => if (defines(key))
               then true -> get(key)
               else false -> null;*/
    
    "The `Category` of all keys for which a value is defined
     by this `Correspondence`."
    see (`function Correspondence.defines`)
    shared default Category<Key> keys
            => object 
            satisfies Category<Key> {
        contains(Key key) => defines(key);
    };
    
    "Determines if this `Correspondence` defines a value for
     every one of the given keys."
    see (`function Correspondence.defines`)
    shared default 
    Boolean definesEvery({Key*} keys) {
        for (key in keys) {
            if (!defines(key)) {
                return false;
            }
        }
        else {
            return true;
        }
    }
    
    "Determines if this `Correspondence` defines a value for
     any one of the given keys."
    see (`function Correspondence.defines`)
    shared default 
    Boolean definesAny({Key*} keys) {
        for (key in keys) {
            if (defines(key)) {
                return true;
            }
        }
        else {
            return false;
        }
    }
    
    "Returns the items defined for the given keys, in the 
     same order as the corresponding keys. For any key which 
     does not have an item defined, the resulting stream 
     contains the value `null`."
    see (`function Correspondence.get`)
    shared default 
    Iterable<Item?,Absent> getAll<Absent>
            (Iterable<Key,Absent> keys) 
            given Absent satisfies Null
            => { for (key in keys) get(key) };
    
}
